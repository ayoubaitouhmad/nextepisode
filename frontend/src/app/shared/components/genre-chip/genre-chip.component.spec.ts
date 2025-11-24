import {ComponentFixture, TestBed} from '@angular/core/testing';

import {GenreChipComponent} from './genre-chip.component';

describe('GenreChipComponent', () => {
  let component: GenreChipComponent;
  let fixture: ComponentFixture<GenreChipComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GenreChipComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(GenreChipComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
