import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { BusinessUnitComponent } from './business-unit.component';
import { BusinessUnitDetailComponent } from './business-unit-detail.component';
import { BusinessUnitPopupComponent } from './business-unit-dialog.component';
import { BusinessUnitDeletePopupComponent } from './business-unit-delete-dialog.component';

export const businessUnitRoute: Routes = [
    {
        path: 'business-unit',
        component: BusinessUnitComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'pipApp.businessUnit.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'business-unit/:id',
        component: BusinessUnitDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'pipApp.businessUnit.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const businessUnitPopupRoute: Routes = [
    {
        path: 'business-unit-new',
        component: BusinessUnitPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'pipApp.businessUnit.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'business-unit/:id/edit',
        component: BusinessUnitPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'pipApp.businessUnit.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'business-unit/:id/delete',
        component: BusinessUnitDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'pipApp.businessUnit.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
