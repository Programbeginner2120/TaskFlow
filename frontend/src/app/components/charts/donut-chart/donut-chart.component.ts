import { Component, computed, HostBinding, inject, input } from "@angular/core";
import { NgxEchartsDirective } from "ngx-echarts";
import { DonutSlice } from "../../../interfaces/charts.interface";
import { EChartsOption } from "echarts/types/dist/shared";
import { ThemeService } from "../../../services/theme.service";
import { Theme } from "../../../interfaces/theme.interface";

@Component({
  selector: 'app-donut-chart',
  standalone: true,
  templateUrl: './donut-chart.component.html',
  styleUrls: ['./donut-chart.component.scss'],
  imports: [NgxEchartsDirective],
})
export class DonutChartComponent {

  @HostBinding('style.height') get hostHeight() { return this.height(); }
  @HostBinding('style.width')  get hostWidth()  { return this.width(); }

  // Required
  data = input.required<DonutSlice[]>();

  // Optional
  title       = input<string>('');
  subtitle    = input<string>('');   // shown in the donut hole
  height      = input<string>('400px');
  width       = input<string>('400px');
  innerRadius = input<string>('55%'); // size of the hole
  outerRadius = input<string>('75%'); // size of the ring
  colors      = input<string[]>([
    '#6366f1', '#22c55e', '#f59e0b',
    '#ef4444', '#3b82f6', '#ec4899',
  ]);

  // injected services
  private readonly themeService = inject(ThemeService);

  theme = computed<Theme>(() => 
    this.themeService.theme()
  );

  chartOptions = computed<EChartsOption>(() => ({
    color: this.colors(),
    title: {
      text: this.title(),
      subtext: this.subtitle(),
      left: 'center',
      top: '40%',
      textStyle: {
        fontSize: 20,
        fontWeight: 'bold',
        color: this.theme() === 'dark' ? 'white' : 'black',
      },
      subtextStyle: {
        fontSize: 14,
        color: this.theme() === 'dark' ? 'white' : 'black',
      },
    },
    tooltip: {
      trigger: 'item',
      formatter: '{b}: {c} ({d}%)',
    },
    legend: {
      orient: 'horizontal',
      bottom: 0,
      data: this.data().map(d => d.name),
      textStyle: {
        color: this.theme() === 'dark' ? 'white' : 'black',
      }
    },
    series: [
      {
        type: 'pie',
        radius: [this.innerRadius(), this.outerRadius()], // [inner, outer] = donut
        center: ['50%', '45%'],
        avoidLabelOverlap: true,
        itemStyle: {
          borderRadius: 6,       // rounded slice corners
          borderColor: '#fff',
          borderWidth: 2,        // gap between slices
        },
        label: {
          show: true,
          position: 'outside',
          formatter: '{b}\n{d}%',
          color: this.theme() === 'dark' ? 'white' : 'black',
        },
        labelLine: {
          show: true,
          length: 10,
          length2: 16,
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 16,
            fontWeight: 'bold',
          },
          scaleSize: 6,          // how much a slice pops out on hover
        },
        data: this.data(),
      },
    ],
  }));
}