/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { PipTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { BusinessUnitDetailComponent } from '../../../../../../main/webapp/app/entities/business-unit/business-unit-detail.component';
import { BusinessUnitService } from '../../../../../../main/webapp/app/entities/business-unit/business-unit.service';
import { BusinessUnit } from '../../../../../../main/webapp/app/entities/business-unit/business-unit.model';

describe('Component Tests', () => {

    describe('BusinessUnit Management Detail Component', () => {
        let comp: BusinessUnitDetailComponent;
        let fixture: ComponentFixture<BusinessUnitDetailComponent>;
        let service: BusinessUnitService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PipTestModule],
                declarations: [BusinessUnitDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    BusinessUnitService,
                    JhiEventManager
                ]
            }).overrideTemplate(BusinessUnitDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(BusinessUnitDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BusinessUnitService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new BusinessUnit(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.businessUnit).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
