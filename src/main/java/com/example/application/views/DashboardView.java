package com.example.application.views;

import com.example.dto.EnrollmentDto;
import com.example.service.*;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import java.util.Map;
import java.util.stream.Collectors;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Dashboard | UniFlow")
@RolesAllowed("ADMIN")
public class DashboardView extends VerticalLayout {
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final SubjectService subjectService;
    private final EnrollmentService enrollmentService;

    public Div statCard(String label, long value) {
        Span number = new Span(String.valueOf(value));
        number.addClassName("stat-number");

        Span caption = new Span(label);
        caption.addClassName("stat-caption");

        Div statCard = new Div();
        statCard.add(number, caption);

        statCard.setClassName("stat-card");
        statCard.setWidthFull();
//        statCard.

        return statCard;
    }

    public DashboardView(StudentService studentService, TeacherService teacherService, SubjectService subjectService, EnrollmentService enrollmentService) {
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.subjectService = subjectService;
        this.enrollmentService = enrollmentService;

        Div students = statCard("Sinh viên", studentService.count());
        Div teachers = statCard("Giảng viên",  teacherService.count());
        Div subjects = statCard("Môn học",  subjectService.count());
        Div enrolls = statCard("Đăng kí",  enrollmentService.count());

        HorizontalLayout cards = new HorizontalLayout(students, teachers, subjects, enrolls);
        cards.setSpacing(true);
        cards.setWidthFull();
        cards.setClassName("cards");

        // Group by theo tên môn học
        Map<String, Long> perSubject = enrollmentService.findAllDto()
                .stream()
                .collect(Collectors
                        .groupingBy(EnrollmentDto::getSubjectName, Collectors.counting()));

        // Biểu đồ cột
//        Chart chart = new Chart(ChartType.COLUMN);
        Chart chart = new Chart(ChartType.COLUMN);
        Configuration conf = chart.getConfiguration(); // lưu cấu hình
        conf.setTitle("Số lượt đăng kí theo môn");

        XAxis x = new XAxis();
        x.setCategories(perSubject.keySet().toArray(new String[0]));
        conf.addxAxis(x);

        YAxis y = new YAxis();
        y.setTitle("Số lượng");
        conf.addyAxis(y);

        ListSeries series = new ListSeries("Sinh viên");
        perSubject.values().forEach(v -> series.addData(v.intValue()));

        conf.addSeries(series);
        chart.setWidthFull();

        add(cards, chart);
    }
}
