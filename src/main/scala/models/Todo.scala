package todo.models

import com.foursquare.rogue.Rogue._

import java.util.regex.Pattern
import net.liftweb.mongodb.record._
import net.liftweb.mongodb.record.field._
import net.liftweb.record.field._
import net.liftweb.record._
import org.bson.types._
import org.joda.time.{DateTime, DateTimeZone}
import java.util.{Date, Calendar}
import net.liftweb.json.JsonParser._
import net.liftweb.json._
import net.liftweb.json.JsonDSL._
import com.mongodb._
import net.liftweb.mongodb.record.MongoRecord

import todo.models.User

object Todos {

  def getOpenByUserId(uid: String): List[Todo] = getOpenByUserId(new ObjectId(uid))

  def getOpenByUserId(uid: ObjectId): List[Todo] = {
    Todo where(_.ownerId eqs uid) and(_.done eqs false) fetch()
  }

  def getAllByUserId(uid: String): List[Todo] = getAllByUserId(new ObjectId(uid))

  def getAllByUserId(uid: ObjectId): List[Todo] = {
    Todo where(_.ownerId eqs uid) fetch()
  }

  def save(j: JObject, uid: ObjectId): Todo = {


    val item = Todo.createRecord
      .done(false)
      .priority(j.values("priority").toString.toInt)

      item.shortdesc.set(j.values.toString)

      item.ownerId.set(uid)

    item.save
  }

  def update(tid: ObjectId, j: JObject, uid: ObjectId) {
    (
      Todo
        .where(_._id eqs tid).and(_.ownerId eqs uid)
        .modify(_.done setTo check2bool(j.values("done").toString)).and(_.priority setTo j.values("priority").toString.toInt).and(_.shortdesc setTo j.values.toString).and(_.modified_at setTo Calendar.getInstance)
        .updateOne()
      )
  }

  def asPubJSON(items: List[Todo]) = {
    compact(render(JArray(items.map(_.asPubJValue))))
  }

  def asPubJSON(items: Todo) = {
    compact(render(items.asPubJValue))
  }

  def check2bool(s: String): Boolean = {
    s match {
      case "true" => true
      case "yes" => true
      case "1" => true
      case "on" => true
      case _ => false
    }
  }

}

class Todo extends MongoRecord[Todo] with MongoId[Todo] {
  def meta = Todo

  object done extends BooleanField(this)

  object ownerId extends ObjectIdField(this) {
    def fetch = User.find("id", value)
  }

  object priority extends IntField(this) {
    override def defaultValue = 5
  }

  object shortdesc extends StringField(this, 128) /* {
    override def validations =
      valMinLen(3, "Description must be 3 characters") _ :: super.validations
  }                                                 */

  object created_at extends DateTimeField(this)

  object modified_at extends DateTimeField(this)


  def asPubJValue: JValue = {
    super.asJValue transform {
      case JObject(xs) => JObject(xs filterNot (Seq("userId") contains _.name))
    }
  }
}

object Todo extends Todo with MongoMetaRecord[Todo] {
  /*override def save = {
    this.created_at(Calendar.getInstance)
    this.modified_at(Calendar.getInstance)
    super.save
  } */
}