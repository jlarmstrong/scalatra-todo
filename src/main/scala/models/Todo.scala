import java.util.regex.Pattern
import net.liftweb.mongodb.record._
import net.liftweb.mongodb.record.field._
import net.liftweb.record.field._
import net.liftweb.record._
import org.bson.types._
import org.joda.time.{DateTime, DateTimeZone}
import net.liftweb.mongodb.record.MongoRecord
import net.liftweb.json.DefaultFormats
import net.liftweb.json.JsonDSL._
import net.liftweb.json.JsonAST.JObject
import java.security.SecureRandom
import java.util.Date
import com.foursquare.rogue.Rogue._

object Todos {
  
  def getOpenByUserId(uid: String): List[Todo] = getOpenByUserId(new ObjectId(uid))
  def getOpenByUserId(uid: ObjectId): List[Todo] = {
    
  }
  
  def getAllByUserId(uid: String): List[Todo] = getAllByUserId(new ObjectId(uid))  
  def getAllByUserId(uid: ObjectId): List[Todo] = {
    
  }
  
  def save(j: JObject,uid: ObjectId): Todo {
    val t = Todo.createRecord
          .done(false)
          .priority(j.values("priority").toInt)
          .desc(j.values.toString)

    t.ownerId.set(uid)
    
    t.save
  }
  
  def update(tid: Objectid,j: JObject,uid: ObjectId) {
    (
      Todo
        .where(_.id eqs tid).and(_.ownerId eqs uid)
        .modify(_.done setTo check2bool(j.values("done"))).and(_.priority setTo j.values("priority").toInt).and(_.desc setTo j.values.toString).and(_.modified_at setTo Calendar.getInstance)
        .updateOne()
    )
  }
  
  def asPubJSON(items: List[Todo]) = {
    compact(render(JArray(items.map(_.asPubJValue))))
  }

  def asPubJSON(items: Todo) = {
    compact(render(items.asPubJValue))
  }
  
  def check2bool(s: String): Boolean ={
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
  object ownerId extends ObjectField(this, User)
  object priority extends IntField(this) {
    override def defaultValue = 5
    override def validations = validPriority _ :: super.validations

    def validPriority(in: Int): List[FieldError] = 
    if (in > 0 && in <=10) Nil
    else List(FieldError(this, <b>Priority must be 1-10</b>))

    override def _toForm = Full(select(ToDo.priorityList,
                                                Full(is.toString),
                                                f => set(f.toInt)))
  }
  object shortdesc extends StringField(this, 128) {
    override def validations =
    valMinLen(3, "Description must be 3 characters") _ :: super.validations
  }
  
  object created_at extends DateTimeField(this)

  object modified_at extends DateTimeField(this)

  
  def asPubJValue: JValue = {
    super.asJValue transform {
      case JObject(xs) => JObject(xs filterNot (Seq("userId") contains _.name))
    }
  }
}

object Todo extends Todo with MongoMetaRecord[Todo] {
  override def save = {
    this.created_at(Calendar.getInstance)
    this.modified_at(Calendar.getInstance)
    super.save
  }
}