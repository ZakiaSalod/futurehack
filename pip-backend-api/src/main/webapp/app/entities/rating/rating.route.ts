import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { RatingComponent } from './rating.component';
import { RatingDetailComponent } from './rating-detail.component';
import { RatingPopupComponent } from './rating-dialog.component';
import { RatingDeletePopupComponent } from './rating-delete-dialog.component';

export const ratingRoute: Routes = [
    {
        path: 'rating',
        component: RatingComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'pipApp.rating.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'rating/:id',
        component: RatingDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'pipApp.rating.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const ratingPopupRoute: Routes = [
    {
        path: 'rating-new',
        component: RatingPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'pipApp.rating.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'rating/:id/edit',
        component: RatingPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'pipApp.rating.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'rating/:id/delete',
        component: RatingDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'pipApp.rating.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
