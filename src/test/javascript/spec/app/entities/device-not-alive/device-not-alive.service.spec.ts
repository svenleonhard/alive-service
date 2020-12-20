import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { DeviceNotAliveService } from 'app/entities/device-not-alive/device-not-alive.service';
import { IDeviceNotAlive, DeviceNotAlive } from 'app/shared/model/device-not-alive.model';

describe('Service Tests', () => {
  describe('DeviceNotAlive Service', () => {
    let injector: TestBed;
    let service: DeviceNotAliveService;
    let httpMock: HttpTestingController;
    let elemDefault: IDeviceNotAlive;
    let expectedResult: IDeviceNotAlive | IDeviceNotAlive[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(DeviceNotAliveService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new DeviceNotAlive(0, currentDate, false);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            occured: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a DeviceNotAlive', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            occured: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            occured: currentDate,
          },
          returnedFromService
        );

        service.create(new DeviceNotAlive()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a DeviceNotAlive', () => {
        const returnedFromService = Object.assign(
          {
            occured: currentDate.format(DATE_TIME_FORMAT),
            confirmed: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            occured: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of DeviceNotAlive', () => {
        const returnedFromService = Object.assign(
          {
            occured: currentDate.format(DATE_TIME_FORMAT),
            confirmed: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            occured: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a DeviceNotAlive', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
