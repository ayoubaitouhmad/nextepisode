import {ComponentFixture, TestBed} from '@angular/core/testing';

import {StreamingSelectorComponentComponent} from './streaming-selector-component.component';

describe('StreamingSelectorComponentComponent', () => {
  let component: StreamingSelectorComponentComponent;
  let fixture: ComponentFixture<StreamingSelectorComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StreamingSelectorComponentComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(StreamingSelectorComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
