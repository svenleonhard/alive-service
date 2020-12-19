import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { AliveMessageService } from 'app/entities/alive-message/alive-message.service';
import { IAliveMessage, AliveMessage } from 'app/shared/model/alive-message.model';

describe('Service Tests', () => {
  describe('AliveMessage Service', () => {
    let injector: TestBed;
    let service: AliveMessageService;
    let httpMock: HttpTestingController;
    let elemDefault: IAliveMessage;
    let expectedResult: IAliveMessage | IAliveMessage[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(AliveMessageService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new AliveMessage(0, currentDate, currentDate, 0);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            sendtime: currentDate.format(DATE_TIME_FORMAT),
            receivetime: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a AliveMessage', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            sendtime: currentDate.format(DATE_TIME_FORMAT),
            receivetime: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            sendtime: currentDate,
            receivetime: currentDate,
          },
          returnedFromService
        );

        service.create(new AliveMessage()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a AliveMessage', () => {
        const returnedFromService = Object.assign(
          {
            sendtime: currentDate.format(DATE_TIME_FORMAT),
            receivetime: currentDate.format(DATE_TIME_FORMAT),
            retrycount: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            sendtime: currentDate,
            receivetime: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of AliveMessage', () => {
        const returnedFromService = Object.assign(
          {
            sendtime: currentDate.format(DATE_TIME_FORMAT),
            receivetime: currentDate.format(DATE_TIME_FORMAT),
            retrycount: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            sendtime: currentDate,
            receivetime: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a AliveMessage', () => {
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
