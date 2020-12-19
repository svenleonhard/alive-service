import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { AliveServiceTestModule } from '../../../test.module';
import { RegisterMessageUpdateComponent } from 'app/entities/register-message/register-message-update.component';
import { RegisterMessageService } from 'app/entities/register-message/register-message.service';
import { RegisterMessage } from 'app/shared/model/register-message.model';

describe('Component Tests', () => {
  describe('RegisterMessage Management Update Component', () => {
    let comp: RegisterMessageUpdateComponent;
    let fixture: ComponentFixture<RegisterMessageUpdateComponent>;
    let service: RegisterMessageService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AliveServiceTestModule],
        declarations: [RegisterMessageUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(RegisterMessageUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RegisterMessageUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(RegisterMessageService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new RegisterMessage(123);
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
        const entity = new RegisterMessage();
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
