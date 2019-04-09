import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { JobShadow } from './job-shadow.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class JobShadowService {

    private resourceUrl = SERVER_API_URL + 'api/job-shadows';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/job-shadows';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(jobShadow: JobShadow): Observable<JobShadow> {
        const copy = this.convert(jobShadow);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(jobShadow: JobShadow): Observable<JobShadow> {
        const copy = this.convert(jobShadow);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<JobShadow> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to JobShadow.
     */
    private convertItemFromServer(json: any): JobShadow {
        const entity: JobShadow = Object.assign(new JobShadow(), json);
        entity.datetime = this.dateUtils
            .convertDateTimeFromServer(json.datetime);
        return entity;
    }

    /**
     * Convert a JobShadow to a JSON which can be sent to the server.
     */
    private convert(jobShadow: JobShadow): JobShadow {
        const copy: JobShadow = Object.assign({}, jobShadow);

        copy.datetime = this.dateUtils.toDate(jobShadow.datetime);
        return copy;
    }
}
