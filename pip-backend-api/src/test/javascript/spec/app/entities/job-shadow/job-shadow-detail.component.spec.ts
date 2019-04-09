/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { PipTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { JobShadowDetailComponent } from '../../../../../../main/webapp/app/entities/job-shadow/job-shadow-detail.component';
import { JobShadowService } from '../../../../../../main/webapp/app/entities/job-shadow/job-shadow.service';
import { JobShadow } from '../../../../../../main/webapp/app/entities/job-shadow/job-shadow.model';

describe('Component Tests', () => {

    describe('JobShadow Management Detail Component', () => {
        let comp: JobShadowDetailComponent;
        let fixture: ComponentFixture<JobShadowDetailComponent>;
        let service: JobShadowService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PipTestModule],
                declarations: [JobShadowDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    JobShadowService,
                    JhiEventManager
                ]
            }).overrideTemplate(JobShadowDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(JobShadowDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(JobShadowService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new JobShadow(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.jobShadow).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
