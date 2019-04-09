import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { JobShadow } from './job-shadow.model';
import { JobShadowService } from './job-shadow.service';

@Component({
    selector: 'jhi-job-shadow-detail',
    templateUrl: './job-shadow-detail.component.html'
})
export class JobShadowDetailComponent implements OnInit, OnDestroy {

    jobShadow: JobShadow;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private jobShadowService: JobShadowService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInJobShadows();
    }

    load(id) {
        this.jobShadowService.find(id).subscribe((jobShadow) => {
            this.jobShadow = jobShadow;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInJobShadows() {
        this.eventSubscriber = this.eventManager.subscribe(
            'jobShadowListModification',
            (response) => this.load(this.jobShadow.id)
        );
    }
}
