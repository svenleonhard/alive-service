import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { AliveServiceTestModule } from '../../../test.module';
import { ObserveUpdateComponent } from 'app/entities/observe/observe-update.component';
import { ObserveService } from 'app/entities/observe/observe.service';
import { Observe } from 'app/shared/model/observe.model';

describe('Component Tests', () => {
  describe('Observe Management Update Component', () => {
    let comp: ObserveUpdateComponent;
    let fixture: ComponentFixture<ObserveUpdateComponent>;
    let service: ObserveService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AliveServiceTestModule],
        declarations: [ObserveUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(ObserveUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ObserveUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ObserveService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Observe(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Observe();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
