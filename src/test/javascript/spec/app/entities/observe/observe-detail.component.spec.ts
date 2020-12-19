import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AliveServiceTestModule } from '../../../test.module';
import { ObserveDetailComponent } from 'app/entities/observe/observe-detail.component';
import { Observe } from 'app/shared/model/observe.model';

describe('Component Tests', () => {
  describe('Observe Management Detail Component', () => {
    let comp: ObserveDetailComponent;
    let fixture: ComponentFixture<ObserveDetailComponent>;
    const route = ({ data: of({ observe: new Observe(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AliveServiceTestModule],
        declarations: [ObserveDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(ObserveDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ObserveDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load observe on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.observe).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
