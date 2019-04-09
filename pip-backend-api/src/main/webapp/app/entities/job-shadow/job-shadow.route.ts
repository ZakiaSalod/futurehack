import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { JobShadowComponent } from './job-shadow.component';
import { JobShadowDetailComponent } from './job-shadow-detail.component';
import { JobShadowPopupComponent } from './job-shadow-dialog.component';
import { JobShadowDeletePopupComponent } from './job-shadow-delete-dialog.component';

export const jobShadowRoute: Routes = [
    {
        path: 'job-shadow',
        component: JobShadowComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'pipApp.jobShadow.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'job-shadow/:id',
        component: JobShadowDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'pipApp.jobShadow.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const jobShadowPopupRoute: Routes = [
    {
        path: 'job-shadow-new',
        component: JobShadowPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'pipApp.jobShadow.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'job-shadow/:id/edit',
        component: JobShadowPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'pipApp.jobShadow.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'job-shadow/:id/delete',
        component: JobShadowDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'pipApp.jobShadow.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
