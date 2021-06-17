package com.example.practiceapplication.ui.fragments

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.example.practiceapplication.R
import com.example.practiceapplication.ui.models.EVENT_FREQUENCY
import com.example.practiceapplication.ui.models.Event
import com.example.practiceapplication.ui.models.REMINDER_FREQUENCY
import com.example.practiceapplication.ui.models.Reminder
import com.example.practiceapplication.ui.viewmodels.CalendarViewModel
import com.example.practiceapplication.utils.AlarmReceiver
import kotlinx.android.synthetic.main.fragment_add_calendar.*
import java.text.DecimalFormat
import java.text.NumberFormat
import java.time.LocalDate.parse
import java.util.*


class AddCalendarFragment : Fragment(), AdapterView.OnItemSelectedListener,
DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private val calendarViewModel: CalendarViewModel by viewModels({requireParentFragment()})

    private lateinit var dateEditText: EditText
    private lateinit var timeEditText: EditText

    private lateinit var description: LinearLayout
    private lateinit var location: LinearLayout
    private lateinit var spinnerFrequency: Spinner

    private lateinit var datePicker: DatePicker
    private var timeInMillis: Long = 0

    private val millisInterval: Map<String, Long> =
        mapOf("ONCE" to 0,
            "EVERY_HALF_HOUR" to AlarmManager.INTERVAL_HALF_HOUR,
            "HOURLY" to AlarmManager.INTERVAL_HOUR,
            "EVERY_3_HOURS" to (AlarmManager.INTERVAL_HOUR * 3),
            "EVERY_12_HOURS" to AlarmManager.INTERVAL_HALF_DAY,
            "DAILY" to AlarmManager.INTERVAL_DAY,
            "WEEKLY" to (AlarmManager.INTERVAL_DAY * 7),
            "MONTHLY" to (AlarmManager.INTERVAL_DAY * 31),
            "YEARLY" to (AlarmManager.INTERVAL_DAY * 365)
        )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_calendar, container, false)
    }

    @ExperimentalStdlibApi
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spinnerType: Spinner = view.findViewById(R.id.calendar_type)
        ArrayAdapter.createFromResource(view.context, R.array.calendar_type_array, android.R.layout.simple_spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerType.adapter = adapter
            }
        spinnerType.onItemSelectedListener = this

        description = view.findViewById(R.id.description)
        location = view.findViewById(R.id.location)

        spinnerFrequency = view.findViewById(R.id.frequency)
        ArrayAdapter.createFromResource(view.context, R.array.event_frequency_array, android.R.layout.simple_spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerFrequency.adapter = adapter
            }
        spinnerFrequency.onItemSelectedListener = this

        val calendar: Calendar = Calendar.getInstance()

        dateEditText = view.findViewById(R.id.add_date)
        dateEditText.setOnClickListener {
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH)
            val year = calendar.get(Calendar.YEAR)
            val datePickerDialog = DatePickerDialog(view.context, this, year, month, day)
            datePickerDialog.show()
        }

        timeEditText = view.findViewById(R.id.add_time)
        timeEditText.setOnClickListener {
            val hour = calendar.get(Calendar.HOUR)
            val minute = calendar.get(Calendar.MINUTE)
            val timePickerDialog = TimePickerDialog(view.context, this, hour, minute,
                DateFormat.is24HourFormat(view.context))
            timePickerDialog.show()
        }

        add_button.setOnClickListener {
            if (spinnerType.selectedItem.toString() == "Event") {
                addEvent(view, calendarViewModel)
            } else {
                addReminder(view, calendarViewModel)
            }
        }

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (parent != null && parent.id == R.id.calendar_type) {
            if (parent.getItemAtPosition(position) == "Event") {
                description.visibility = View.VISIBLE
                location.visibility = View.VISIBLE
                if (view != null) {
                    ArrayAdapter.createFromResource(view.context, R.array.event_frequency_array, android.R.layout.simple_spinner_item)
                        .also { adapter ->
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            spinnerFrequency.adapter = adapter
                        }
                }
                spinnerFrequency.onItemSelectedListener = this
                add_button.setText(R.string.add_event)
            } else if (parent.getItemAtPosition(position) == "Reminder") {
                description.visibility = View.GONE
                location.visibility = View.GONE
                if (view != null) {
                    ArrayAdapter.createFromResource(view.context, R.array.reminder_frequency_array, android.R.layout.simple_spinner_item)
                        .also { adapter ->
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            spinnerFrequency.adapter = adapter
                        }
                }
                spinnerFrequency.onItemSelectedListener = this
                add_button.setText(R.string.add_reminder)
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addEvent(view: View, calendarViewModel: CalendarViewModel) {
        val titlePlace = view.findViewById<EditText>(R.id.add_title)
        val title = titlePlace.text.toString()

        val descriptionPlace = view.findViewById<EditText>(R.id.add_description)
        val description = descriptionPlace.text.toString()

        val locationPlace = view.findViewById<EditText>(R.id.add_location)
        val location = locationPlace.text.toString()

        val datePlace = view.findViewById<EditText>(R.id.add_date)
        val date = parse(datePlace.text.toString())

        val timePlace = view.findViewById<EditText>(R.id.add_time)
        val time = timePlace.text.toString()

        val frequencyPlace = view.findViewById<Spinner>(R.id.frequency)
        val frequency = EVENT_FREQUENCY.values()[frequencyPlace.selectedItemPosition]

        if (title.isBlank() || description.isBlank() || location.isBlank() || time.isBlank()){
            Toast.makeText(view.context,"Enter value!",Toast.LENGTH_LONG).show()
        } else {
            val event = Event(title, description, location, date, time, frequency != EVENT_FREQUENCY.ONCE, frequency)
            calendarViewModel.addEvent(event)

            val intent = prepareIntent(view, event)
            setAlarm(millisInterval.getValue(frequency.toString()), view, intent)

            parentFragmentManager.commit {
                replace(R.id.calendar_container_view, ViewCalendarFragment())
                setReorderingAllowed(true)
                addToBackStack("viewCalendar") // name can be null
            }
        }
    }

    @ExperimentalStdlibApi
    @RequiresApi(Build.VERSION_CODES.O)
    private fun addReminder(view: View, calendarViewModel: CalendarViewModel) {
        val titlePlace = view.findViewById<EditText>(R.id.add_title)
        val title = titlePlace.text.toString()

        val datePlace = view.findViewById<EditText>(R.id.add_date)
        val date = parse(datePlace.text.toString())

        val timePlace = view.findViewById<EditText>(R.id.add_time)
        val time = timePlace.text.toString()

        val frequencyPlace = view.findViewById<Spinner>(R.id.frequency)
        val frequency = REMINDER_FREQUENCY.values()[frequencyPlace.selectedItemPosition]

        if (title.isBlank() || time.isBlank()){
            Toast.makeText(view.context,"Enter value!",Toast.LENGTH_LONG).show()
        } else {
            val reminder = Reminder(title, date, time, frequency != REMINDER_FREQUENCY.ONCE, frequency)
            calendarViewModel.addReminder(reminder)

            val intent = prepareIntent(view, reminder)
            setAlarm(millisInterval.getValue(frequency.toString()), view, intent)

            parentFragmentManager.commit {
                replace(R.id.calendar_container_view, ViewCalendarFragment())
                setReorderingAllowed(true)
                addToBackStack("viewCalendar") // name can be null
            }
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val twoDigit: NumberFormat = DecimalFormat("00")
        dateEditText.setText("$year-${twoDigit.format(month + 1)}-${twoDigit.format(dayOfMonth)}")
        if (view != null) {
            datePicker = view
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val twoDigit: NumberFormat = DecimalFormat("00")
        timeEditText.setText("${twoDigit.format(hourOfDay)}:${twoDigit.format(minute)}")
        val cal = Calendar.getInstance()
        if (view != null) {
            cal.set(datePicker.year, datePicker.month, datePicker.dayOfMonth, view.hour, view.minute)
            timeInMillis = cal.timeInMillis
        }
    }

    private fun prepareIntent(view: View, event: Event): Intent {
        val intent = Intent(view.context, AlarmReceiver::class.java)
        intent.putExtra("title", event.title)
        intent.putExtra("description", event.location)
        return intent
    }

    @ExperimentalStdlibApi
    private fun prepareIntent(view: View, reminder: Reminder): Intent {
        val intent = Intent(view.context, AlarmReceiver::class.java)
        intent.putExtra("title", reminder.title)
        intent.putExtra("description", "Remember to " +
                reminder.title +
                reminder.frequency.toString().replace("_", " ").lowercase().capitalize(Locale.ROOT))
        return intent
    }

    private fun setAlarm(frequency: Long, view: View, intent: Intent) {
        val alarmManager = view.context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = PendingIntent.getBroadcast(view.context, Date().time.toInt(), intent, 0)
        val zero: Int = 0
        if (frequency == zero.toLong()) {
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                timeInMillis,
                pendingIntent
            )
        } else {
            Log.i("here", "repeating")
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                timeInMillis,
                frequency,
                pendingIntent
            )
        }
    }
}