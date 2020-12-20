import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AliveServiceTestModule } from '../../../test.module';
import { DeviceNotAliveDetailComponent } from 'app/entities/device-not-alive/device-not-alive-detail.component';
import { DeviceNotAlive } from 'app/shared/model/device-not-alive.model';

describe('Component Tests', () => {
  describe('DeviceNotAlive Management Detail Component', () => {
    let comp: DeviceNotAliveDetailComponent;
    let fixture: ComponentFixture<DeviceNotAliveDetailComponent>;
    const route = ({ data: of({ deviceNotAlive: new DeviceNotAlive(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AliveServiceTestModule],
        declarations: [DeviceNotAliveDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(DeviceNotAliveDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DeviceNotAliveDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load deviceNotAlive on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.deviceNotAlive).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
