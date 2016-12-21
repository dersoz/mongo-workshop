import com.mongodb.MongoClient
import com.mongodb.client.MongoDatabase
import org.bson.Document
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MongoFarmTest {

    val config = ("localhost" to 27017)
    val mongo: MongoClient by lazy { MongoClient(config.first, config.second) }
    lateinit var db: MongoDatabase
    val dbName = "MongoFarm"
    val c1Name = "animals"

    @Before
    fun before() {
        db = mongo.getDatabase(dbName)
        db.createCollection(c1Name)
    }

    @After
    fun after() {
        db.drop()
    }

    @Test
    fun shouldListCollectionsInServer() {
        assertTrue(db.listCollectionNames().count() > 0)
        db.listCollectionNames().forEach { println("Collection: " + it) }
    }

    @Test
    fun shouldInsertOneElement() {
        val c1 = db.getCollection(c1Name)
        val chick = buildMember("tuylu", "chicken", 5, 6)
        c1.insertOne(chick)
        assertEquals(1, c1.count())
    }

    @Test
    fun insertMany() {
        val c1 = db.getCollection(c1Name)
        val familyMembers = members()
        c1.insertMany(familyMembers)
        assertEquals(4, c1.count())
        c1.find().forEach(::println)
        c1.find()
        println(c1.namespace.fullName)
    }

    private fun buildMember(name: String, type: String, age: Int, weight: Int): Document {
        val member = Document()
        member["name"] = name
        member["type"] = type
        member["attributes"] = buildAttributes(age, weight)
        return member
    }

    private fun buildAttributes(age: Int, weight: Int) =
            Document().apply {
                put("age", age)
                put("weight", weight)
            }

    private fun members() =
            mutableListOf(
                    buildMember("sarikiz", "cow", 8, 1009),
                    buildMember("fatos", "sheep", 5, 75),
                    buildMember("george", "goat", 3, 27),
                    buildMember("tully", "rooster", 3, 7)
            )

}