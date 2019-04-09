import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { PipRatingModule } from './rating/rating.module';
import { PipBusinessUnitModule } from './business-unit/business-unit.module';
import { PipJobOpportunityModule } from './job-opportunity/job-opportunity.module';
import { PipJobShadowModule } from './job-shadow/job-shadow.module';
import { PipAchievementModule } from './achievement/achievement.module';
import { PipIndustryModule } from './industry/industry.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        PipRatingModule,
        PipBusinessUnitModule,
        PipJobOpportunityModule,
        PipJobShadowModule,
        PipAchievementModule,
        PipIndustryModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PipEntityModule {}
