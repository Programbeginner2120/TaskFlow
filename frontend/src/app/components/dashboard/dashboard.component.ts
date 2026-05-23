import { ChangeDetectionStrategy, Component, computed, signal } from "@angular/core";
import { MultiLineChartComponent } from "../charts/multi-line-chart/multi-line-chart.component";
import { BarSeriesData, DonutSlice, GaugeThreshold, LineSeriesData } from "../../interfaces/charts.interface";
import { BarChartComponent } from "../charts/bar-chart/bar-chart.component";
import { ProgressGaugeComponent } from "../charts/progress-gauge-chart/progress-gauge-chart.component";
import { DonutChartComponent } from "../charts/donut-chart/donut-chart.component";
import { DashboardFilterComponent } from "./dashboard-filter/dashboard-filter.component";
import { StatisticsCard, TaskTableRow } from "../../interfaces/dashboard.interface";
import { Activity, AlertTriangle, CheckCircle2, Clock, ListTodo, LucideAngularModule } from "lucide-angular";
import { TaskTableComponent } from "./task-table/task-table.component";

@Component({
    selector: 'app-dashboard',
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.scss'],
    imports: [MultiLineChartComponent, BarChartComponent, ProgressGaugeComponent, DonutChartComponent, DashboardFilterComponent, LucideAngularModule, TaskTableComponent],
    changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DashboardComponent {

    // stats card info
    statisticsCards = signal<StatisticsCard[]>([
        { label: 'Total', value: 10, iconBackgroundColor: '#EFEFFD', icon: ListTodo },
        { label: 'Completed', value: 1, iconBackgroundColor: '#E8F9EE', icon: CheckCircle2 },
        { label: 'Active', value: 9, iconBackgroundColor: '#EFEFFD', icon: Activity },
        { label: 'Overdue', value: 0, iconBackgroundColor: '#FDECEC', icon: AlertTriangle },
        { label: 'Due Today', value: 2,iconBackgroundColor: '#FDF5E6', icon: Clock }
    ]);

    // progress gauge info

    cpu = signal(43);

    // donut chart info

    regionData = signal<DonutSlice[]>([
        { name: 'North',  value: 1048 },
        { name: 'South',  value: 735  },
        { name: 'East',   value: 580  },
        { name: 'West',   value: 484  },
    ]);

    // bar chart info

    quarters = signal(['Q1', 'Q2', 'Q3', 'Q4']);

    salesSeries = signal<BarSeriesData[]>([
        { name: 'North', data: [320, 440, 390, 510] },
        { name: 'South', data: [220, 310, 280, 390] },
        { name: 'East',  data: [150, 200, 175, 260] },
    ]);

    // multi line chart info

    months = signal(['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun']);

    revenueSeries = signal<LineSeriesData[]>([
        { name: 'Product A', data: [120, 200, 150,  80,  70, 110] },
        { name: 'Product B', data: [220, 182, 191, 234, 290, 330] },
        { name: 'Product C', data: [150, 232, 201, 154, 190, 330] },
    ]);

    // task table info

    taskTableRows = signal<TaskTableRow[]>([
        { title: 'Fix login page bug',             listName: 'Work',     listColor: '#6366F1', dueDate: new Date('2026-05-23'), status: 'Active' },
        { title: 'Prepare sprint retrospective',   listName: 'Work',     listColor: '#6366F1', dueDate: new Date('2026-05-26'), status: 'Active' },
        { title: 'Buy groceries',                  listName: 'Shopping', listColor: '#F59E0B', dueDate: new Date('2026-05-24'), status: 'Active' },
        { title: 'Deploy staging environment',     listName: 'Work',     listColor: '#6366F1', dueDate: new Date('2026-05-28'), status: 'Active' },
        { title: 'Review Q2 roadmap presentation', listName: 'Work',     listColor: '#6366F1', dueDate: new Date('2026-05-23'), status: 'Active' },
        { title: 'Order new running shoes',        listName: 'Shopping', listColor: '#F59E0B', dueDate: new Date('2026-05-30'), status: 'Active' },
        { title: 'Schedule dentist appointment',   listName: 'Personal', listColor: '#10B981', dueDate: new Date('2026-05-25'), status: 'Active' },
        { title: 'Plan weekend hiking trip',       listName: 'Personal', listColor: '#10B981', dueDate: new Date('2026-05-30'), status: 'Active' },
        { title: 'Fix login page bug',             listName: 'Work',     listColor: '#6366F1', dueDate: new Date('2026-05-23'), status: 'Active' },
        { title: 'Prepare sprint retrospective',   listName: 'Work',     listColor: '#6366F1', dueDate: new Date('2026-05-26'), status: 'Active' },
        { title: 'Buy groceries',                  listName: 'Shopping', listColor: '#F59E0B', dueDate: new Date('2026-05-24'), status: 'Active' },
        { title: 'Deploy staging environment',     listName: 'Work',     listColor: '#6366F1', dueDate: new Date('2026-05-28'), status: 'Active' },
        { title: 'Review Q2 roadmap presentation', listName: 'Work',     listColor: '#6366F1', dueDate: new Date('2026-05-23'), status: 'Active' },
        { title: 'Order new running shoes',        listName: 'Shopping', listColor: '#F59E0B', dueDate: new Date('2026-05-30'), status: 'Active' },
        { title: 'Schedule dentist appointment',   listName: 'Personal', listColor: '#10B981', dueDate: new Date('2026-05-25'), status: 'Active' },
        { title: 'Plan weekend hiking trip',       listName: 'Personal', listColor: '#10B981', dueDate: new Date('2026-05-30'), status: 'Active' },
        { title: 'Fix login page bug',             listName: 'Work',     listColor: '#6366F1', dueDate: new Date('2026-05-23'), status: 'Active' },
        { title: 'Prepare sprint retrospective',   listName: 'Work',     listColor: '#6366F1', dueDate: new Date('2026-05-26'), status: 'Active' },
        { title: 'Buy groceries',                  listName: 'Shopping', listColor: '#F59E0B', dueDate: new Date('2026-05-24'), status: 'Active' },
        { title: 'Deploy staging environment',     listName: 'Work',     listColor: '#6366F1', dueDate: new Date('2026-05-28'), status: 'Active' },
        { title: 'Review Q2 roadmap presentation', listName: 'Work',     listColor: '#6366F1', dueDate: new Date('2026-05-23'), status: 'Active' },
        { title: 'Order new running shoes',        listName: 'Shopping', listColor: '#F59E0B', dueDate: new Date('2026-05-30'), status: 'Active' },
        { title: 'Schedule dentist appointment',   listName: 'Personal', listColor: '#10B981', dueDate: new Date('2026-05-25'), status: 'Active' },
        { title: 'Plan weekend hiking trip',       listName: 'Personal', listColor: '#10B981', dueDate: new Date('2026-05-30'), status: 'Active' },
        { title: 'Fix login page bug',             listName: 'Work',     listColor: '#6366F1', dueDate: new Date('2026-05-23'), status: 'Active' },
        { title: 'Prepare sprint retrospective',   listName: 'Work',     listColor: '#6366F1', dueDate: new Date('2026-05-26'), status: 'Active' },
        { title: 'Buy groceries',                  listName: 'Shopping', listColor: '#F59E0B', dueDate: new Date('2026-05-24'), status: 'Active' },
        { title: 'Deploy staging environment',     listName: 'Work',     listColor: '#6366F1', dueDate: new Date('2026-05-28'), status: 'Active' },
        { title: 'Review Q2 roadmap presentation', listName: 'Work',     listColor: '#6366F1', dueDate: new Date('2026-05-23'), status: 'Active' },
        { title: 'Order new running shoes',        listName: 'Shopping', listColor: '#F59E0B', dueDate: new Date('2026-05-30'), status: 'Active' },
        { title: 'Schedule dentist appointment',   listName: 'Personal', listColor: '#10B981', dueDate: new Date('2026-05-25'), status: 'Active' },
        { title: 'Plan weekend hiking trip',       listName: 'Personal', listColor: '#10B981', dueDate: new Date('2026-05-30'), status: 'Active' },
    ]);

}