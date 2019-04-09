/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { PipTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { JobOpportunityDetailComponent } from '../../../../../../main/webapp/app/entities/job-opportunity/job-opportunity-detail.component';
import { JobOpportunityService } from '../../../../../../main/webapp/app/entities/job-opportunity/job-opportunity.service';
import { JobOpportunity } from '../../../../../../main/webapp/app/entities/job-opportunity/job-opportunity.model';

describe('Component Tests', () => {

    describe('JobOpportunity Management Detail Component', () => {
        let comp: JobOpportunityDetailComponent;
        let fixture: ComponentFixture<JobOpportunityDetailComponent>;
        let service: JobOpportunityService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PipTestModule],
                declarations: [JobOpportunityDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    JobOpportunityService,
                    JhiEventManager
                ]
            }).overrideTemplate(JobOpportunityDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(JobOpportunityDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(JobOpportunityService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new JobOpportunity(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.jobOpportunity).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
