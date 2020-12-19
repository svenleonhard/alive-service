import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AliveServiceTestModule } from '../../../test.module';
import { RegisterMessageDetailComponent } from 'app/entities/register-message/register-message-detail.component';
import { RegisterMessage } from 'app/shared/model/register-message.model';

describe('Component Tests', () => {
  describe('RegisterMessage Management Detail Component', () => {
    let comp: RegisterMessageDetailComponent;
    let fixture: ComponentFixture<RegisterMessageDetailComponent>;
    const route = ({ data: of({ registerMessage: new RegisterMessage(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AliveServiceTestModule],
        declarations: [RegisterMessageDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(RegisterMessageDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RegisterMessageDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load registerMessage on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.registerMessage).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
