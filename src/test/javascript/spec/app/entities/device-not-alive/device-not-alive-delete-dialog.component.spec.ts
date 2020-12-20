import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { AliveServiceTestModule } from '../../../test.module';
import { MockEventManager } from '../../../helpers/mock-event-manager.service';
import { MockActiveModal } from '../../../helpers/mock-active-modal.service';
import { DeviceNotAliveDeleteDialogComponent } from 'app/entities/device-not-alive/device-not-alive-delete-dialog.component';
import { DeviceNotAliveService } from 'app/entities/device-not-alive/device-not-alive.service';

describe('Component Tests', () => {
  describe('DeviceNotAlive Management Delete Component', () => {
    let comp: DeviceNotAliveDeleteDialogComponent;
    let fixture: ComponentFixture<DeviceNotAliveDeleteDialogComponent>;
    let service: DeviceNotAliveService;
    let mockEventManager: MockEventManager;
    let mockActiveModal: MockActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AliveServiceTestModule],
        declarations: [DeviceNotAliveDeleteDialogComponent],
      })
        .overrideTemplate(DeviceNotAliveDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DeviceNotAliveDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DeviceNotAliveService);
      mockEventManager = TestBed.get(JhiEventManager);
      mockActiveModal = TestBed.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.closeSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
      });
    });
  });
});
