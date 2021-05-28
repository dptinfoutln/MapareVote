import {AfterViewInit, Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {environment} from '../../../environments/environment';
import {VotesService} from '../../services/votes.service';

declare var $: any;

@Component({
    selector: 'app-filter',
    templateUrl: './filter.component.html',
    styleUrls: ['./filter.component.scss']
})
export class FilterComponent implements OnInit {

    pageNum = 1;
    pageSize = environment.defaultPageSize;
    voteCount;
    pageCount;
    pageNumbers = [];
    defaultPageSize = environment.defaultPageSize;
    orderBy = 'asc';
    sortBy;
    nameLike;
    open = true;
    private initiated = false;

    constructor(private route: ActivatedRoute,
                private router: Router,
                private votesService: VotesService) {
    }

    ngOnInit(): void {
        this.votesService.voteCount.subscribe(voteCount => {
            this.voteCount = Number(voteCount);
        });
        this.votesService.pageCount.subscribe(pageCount => {
            this.pageCount = Number(pageCount);
            this.generateMinMaxPages();
        });
        this.route.queryParams.subscribe(params => {
            if (Number(params.page_num) >= 1) {
                this.pageNum = Number(params.page_num);
            }
            if (Number(params.page_size) >= 1) {
                this.pageSize = Number(params.page_size);
            } else {
                this.pageSize = environment.defaultPageSize;
            }
            if (params.order) {
                this.orderBy = params.order;
            } else {
                this.orderBy = 'asc';
            }
            this.sortBy = params.sort;
            if (params.name) {
                this.nameLike = params.name;
                $('#searchInput').val(params.name);
            }
            this.open = (params.open !== 'false');
            this.initiated = true;
        });
    }

    generateMinMaxPages(): void {
        let minPage;
        let maxPage;
        const nbElem = 2;
        this.pageNumbers = [];
        if (this.pageNum < nbElem + 1) {
            minPage = 1;
        } else {
            minPage = this.pageNum - nbElem;
        }
        if (this.pageCount > this.pageNum + nbElem){
            maxPage = this.pageNum + nbElem;
        } else {
            maxPage = this.pageCount;
        }
        for (let i = minPage; i <= maxPage; i ++) {
            this.pageNumbers.push(i);
        }
    }

    onSearch(): void {
        this.router.navigate([],
            {queryParams: {page_num: 1, name: $('#searchInput').val()}, queryParamsHandling: 'merge'});
    }

    reversOpenFilter(): void {
        this.open = !this.open;
    }

    onOpenFilter(): void {
        this.reversOpenFilter();
        this.router.navigate([],
            {queryParams: {page_num: 1, open: this.open}, queryParamsHandling: 'merge'});
    }

    onPageSize(): void {
        this.router.navigate([],
            {queryParams: {page_num: 1, page_size: $('#pageSizeRange').val()}, queryParamsHandling: 'merge'});
    }
}
