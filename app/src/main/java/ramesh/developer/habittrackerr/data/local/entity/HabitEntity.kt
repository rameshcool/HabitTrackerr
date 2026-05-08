package ramesh.developer.habittrackerr.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    @ColumnInfo(name = "icon_name")    val iconName: String,
    @ColumnInfo(name = "monday")       val monday: Boolean,
    @ColumnInfo(name = "tuesday")      val tuesday: Boolean,
    @ColumnInfo(name = "wednesday")    val wednesday: Boolean,
    @ColumnInfo(name = "thursday")     val thursday: Boolean,
    @ColumnInfo(name = "friday")       val friday: Boolean,
    @ColumnInfo(name = "saturday")     val saturday: Boolean,
    @ColumnInfo(name = "sunday")       val sunday: Boolean,
    @ColumnInfo(name = "created_date") val createdDate: Long  // epoch day (LocalDate.toEpochDay())
)
