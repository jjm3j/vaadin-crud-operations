package com.jjmj.application.views.views;

import com.jjmj.application.data.service.BookService;
import com.jjmj.application.data.service.StyleService;
import com.jjmj.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;

@PermitAll
@Route(value = "dashboard", layout = MainLayout.class)
@PageTitle("Диаграмма")
public class DashboardView extends VerticalLayout {
    private final BookService bookService;
    private final StyleService styleService;

    public DashboardView(BookService bookService, StyleService styleService) {
        this.bookService = bookService;
        this.styleService = styleService;
        addClassName("dashboard-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        add(getBookStats(), getStylesChart());
    }

    private Component getBookStats() {
        var stats = new Span(String.valueOf(bookService.count())+" книг(и)");
        stats.addClassNames("text-xl", "mt-m");
        return stats;
    }

    private Chart getStylesChart() {
        var chart = new Chart(ChartType.PIE);
        var dataSeries = new DataSeries();
        styleService.findAllStyles().forEach(style -> dataSeries.add(new DataSeriesItem(style.getName(), style.getBooksCount())));
        chart.getConfiguration().setSeries(dataSeries);
        return chart;
    }
}
