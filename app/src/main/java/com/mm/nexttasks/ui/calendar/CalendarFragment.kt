package com.mm.nexttasks.ui.calendar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.ViewContainer
import com.mm.nexttasks.R
import com.mm.nexttasks.databinding.CalendarDayViewBinding
import com.mm.nexttasks.databinding.FragmentCalendarBinding
import java.time.DayOfWeek
import java.time.YearMonth

class CalendarFragment : Fragment() {
    private lateinit var binding: FragmentCalendarBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalendarBinding.inflate(inflater, container, false)

        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(100)
        val endMonth = currentMonth.plusMonths(100)
        val daysOfWeek = daysOfWeek(firstDayOfWeek = firstDayOfWeekFromLocale())

        configureBinders(daysOfWeek)
        binding.calendar.setup(startMonth, endMonth, daysOfWeek.first())
        binding.calendar.scrollToMonth(currentMonth)

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun configureBinders(daysOfWeek: List<DayOfWeek>) {
        class DayViewContainer(view: View) : ViewContainer(view) {
            val dayTextView = CalendarDayViewBinding.bind(view).dayText
        }

        binding.calendar.dayBinder = object : MonthDayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)

            override fun bind(container: DayViewContainer, data: CalendarDay) {
                container.dayTextView.text = data.date.dayOfMonth.toString()
            }
        }
    }
}