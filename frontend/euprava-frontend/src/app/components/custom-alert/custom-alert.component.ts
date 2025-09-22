import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-custom-alert',
  templateUrl: './custom-alert.component.html',
  styleUrls: ['./custom-alert.component.css']
})
export class CustomAlertComponent {
  @Input() message: string = '';
  @Input() type: 'success' | 'error' = 'error';
  @Output() closed = new EventEmitter<void>();

  close() {
    this.closed.emit();
  }
}
