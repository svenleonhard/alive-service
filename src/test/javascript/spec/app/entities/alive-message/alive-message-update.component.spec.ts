import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { AliveServiceTestModule } from '../../../test.module';
import { AliveMessageUpdateComponent } from 'app/entities/alive-message/alive-message-update.component';
import { AliveMessageService } from 'app/entities/alive-message/alive-message.service';
import { AliveMessage } from 'app/shared/model/alive-message.model';

describe('Component Tests', () => {
  describe('AliveMessage Management Update Component', () => {
    let comp: AliveMessageUpdateComponent;
    let fixture: ComponentFixture<AliveMessageUpdateComponent>;
    let service: AliveMessageService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AliveServiceTestModule],
        declarations: [AliveMessageUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(AliveMessageUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AliveMessageUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AliveMessageService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new AliveMessage(123);
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
        const entity = new AliveMessage();
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
