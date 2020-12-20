import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { AliveServiceTestModule } from '../../../test.module';
import { DeviceNotAliveUpdateComponent } from 'app/entities/device-not-alive/device-not-alive-update.component';
import { DeviceNotAliveService } from 'app/entities/device-not-alive/device-not-alive.service';
import { DeviceNotAlive } from 'app/shared/model/device-not-alive.model';

describe('Component Tests', () => {
  describe('DeviceNotAlive Management Update Component', () => {
    let comp: DeviceNotAliveUpdateComponent;
    let fixture: ComponentFixture<DeviceNotAliveUpdateComponent>;
    let service: DeviceNotAliveService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AliveServiceTestModule],
        declarations: [DeviceNotAliveUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(DeviceNotAliveUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DeviceNotAliveUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DeviceNotAliveService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new DeviceNotAlive(123);
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
        const entity = new DeviceNotAlive();
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
