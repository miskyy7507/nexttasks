package com.mm.nexttasks.ui.calendar

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.kizitonwose.calendar.view.ViewContainer
import com.mm.nexttasks.R
import com.mm.nexttasks.databinding.CalendarDayViewBinding
import com.mm.nexttasks.databinding.FragmentCalendarBinding
import com.mm.nexttasks.ui.taskList.TaskListFragment
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.TextStyle
import java.util.Date
import java.util.Locale

private lateinit var selectedDate: LocalDate
class CalendarFragment : Fragment() {
    private lateinit var binding: FragmentCalendarBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalendarBinding.inflate(inflater, container, false)

        val toolbar = (activity as AppCompatActivity).supportActionBar!!

        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(100)
        val endMonth = currentMonth.plusMonths(100)
        val daysOfWeek = daysOfWeek()

        selectedDate = LocalDate.now()

        configureBinders(daysOfWeek)
        binding.calendar.setup(startMonth, endMonth, daysOfWeek.first())
        binding.calendar.scrollToMonth(currentMonth)

        binding.calendar.monthScrollListener = {
            toolbar.title = it.yearMonth.toString()
        }

        updateList(selectedDate)

        return binding.root
    }

    private fun updateList(date: LocalDate) {
        val timestamp = date.atStartOfDay().toInstant(ZoneOffset.ofHours(2)).toEpochMilli()

        Log.d(null, timestamp.toString())
        Log.d(null, Date(timestamp).toString())

        binding.fragmentContainerView.getFragment<TaskListFragment>()

        childFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, TaskListFragment.newInstance(timestamp))
            .commit()
    }

    private fun configureBinders(daysOfWeek: List<DayOfWeek>) {
        class DayViewContainer(view: View) : ViewContainer(view) {
            val dayTextView = CalendarDayViewBinding.bind(view).dayText

            lateinit var day: CalendarDay

            init {
                view.setOnClickListener {
                    if (day.position != DayPosition.MonthDate) {
                        return@setOnClickListener
                    }
                    selectedDate = day.date
                    binding.calendar.notifyCalendarChanged()
                    updateList(selectedDate)
                }
            }
        }

        class MonthViewContainer(view: View) : ViewContainer(view) {
            val titlesContainer = view as ViewGroup
        }

        binding.calendar.dayBinder = object : MonthDayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)

            override fun bind(container: DayViewContainer, data: CalendarDay) {
                container.dayTextView.text = data.date.dayOfMonth.toString()
                container.day = data

                container.dayTextView.alpha = if (data.position == DayPosition.MonthDate) 1.0f else 0.3f

                if (data.date == selectedDate) {
                    container.dayTextView.setTextColor(requireContext().getColor(R.color.app_theme_color))
                }
                else {
                    container.dayTextView.setTextColor(Color.BLACK)
                }
            }
        }

        binding.calendar.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, data: CalendarMonth) {
                // Remember that the header is reused so this will be called for each month.
                // However, the first day of the week will not change so no need to bind
                // the same view every time it is reused.
                if (container.titlesContainer.tag == null) {
                    container.titlesContainer.tag = data.yearMonth
                    container.titlesContainer.children.map { it as TextView }
                        .forEachIndexed { index, textView ->
                            val dayOfWeek = daysOfWeek[index]
                            val title = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                            textView.text = title
                        }
                }
            }
        }
    }
}