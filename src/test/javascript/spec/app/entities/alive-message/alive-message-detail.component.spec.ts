import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AliveServiceTestModule } from '../../../test.module';
import { AliveMessageDetailComponent } from 'app/entities/alive-message/alive-message-detail.component';
import { AliveMessage } from 'app/shared/model/alive-message.model';

describe('Component Tests', () => {
  describe('AliveMessage Management Detail Component', () => {
    let comp: AliveMessageDetailComponent;
    let fixture: ComponentFixture<AliveMessageDetailComponent>;
    const route = ({ data: of({ aliveMessage: new AliveMessage(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AliveServiceTestModule],
        declarations: [AliveMessageDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(AliveMessageDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AliveMessageDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load aliveMessage on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.aliveMessage).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
