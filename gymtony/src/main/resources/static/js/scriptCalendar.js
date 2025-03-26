var monthNames = ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'];

var currentDate = new Date();
var currentDay = currentDate.getDate();
var currentMonth = currentDate.getMonth();
var currentYear = currentDate.getFullYear();

var dates = document.getElementById('dates');
var month = document.getElementById('month');
var year = document.getElementById('year');

var prevMonthDOM = document.getElementById('prev_month');
var nextMonthDOM = document.getElementById('next_month');

var displayedMonth = currentMonth;
var displayedYear = currentYear;

month.textContent = monthNames[displayedMonth];
year.textContent = displayedYear.toString();

prevMonthDOM.addEventListener('click', () => lastMonth());
nextMonthDOM.addEventListener('click', () => nextMonth());

writeMonth(displayedMonth);

function writeMonth(month) {
    dates.innerHTML = ''; 
    var totalDays = getTotalDays(month);
    var prevMonthDays = getTotalDays(month - 1);

    for (var i = startDay(); i > 0; i--) {
        dates.innerHTML += '<div class="calendar_date calendar_item calendar_last_days">' + (prevMonthDays - i + 1) + '</div>';
    }

    for (var i = 1; i <= totalDays; i++) {
        if (i === currentDay && displayedMonth === currentMonth && displayedYear === currentYear) {
            dates.innerHTML += '<div class="calendar_date calendar_item calendar_today">' + i + '</div>';
        } else {
            dates.innerHTML += '<div class="calendar_date calendar_item">' + i + '</div>';
        }
    }
}

function getTotalDays(month) {
    if (month === -1) month = 11;

    if (month == 0 || month == 2 || month == 4 || month == 6 || month == 7 || month == 9 || month == 11) {
        return 31;
    } else if (month == 3 || month == 5 || month == 8 || month == 10) {
        return 30;
    } else {
        return isLeap() ? 29 : 28;
    }
}

function isLeap() {
    return ((currentYear % 100 !== 0) && (currentYear % 4 === 0) || (currentYear % 400 === 0));
}

function startDay() {
    var start = new Date(displayedYear, displayedMonth, 1);
    return (start.getDay() === 0) ? 6 : start.getDay() - 1;
}

function lastMonth() {
    if (displayedMonth !== 0) {
        displayedMonth--;
    } else {
        displayedMonth = 11;
        displayedYear--;
    }

    setNewDate();
}

function nextMonth() {
    if (displayedMonth !== 11) {
        displayedMonth++;
    } else {
        displayedMonth = 0;
        displayedYear++;
    }

    setNewDate();
}

function setNewDate() {
    currentDate.setFullYear(displayedYear, displayedMonth, currentDay);
    month.textContent = monthNames[displayedMonth];
    year.textContent = displayedYear.toString();
    dates.textContent = '';
    writeMonth(displayedMonth);
}
