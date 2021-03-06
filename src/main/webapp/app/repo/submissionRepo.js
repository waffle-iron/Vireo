vireo.repo("SubmissionRepo", function SubmissionRepo($q, FileService, WsApi, Submission) {

    var submissionRepo = this;

    // additional repo methods and variables

    submissionRepo.findSubmissionById = function (id) {
        var submission = submissionRepo.findById(id);
        var defer = $q.defer();
        if (!submission) {
            submissionRepo.clearValidationResults();
            angular.extend(submissionRepo.mapping.one, {
                'method': 'get-one/' + id
            });
            var fetchPromise = WsApi.fetch(submissionRepo.mapping.one);
            fetchPromise.then(function (res) {
                var resObj = angular.fromJson(res.body);
                if (resObj.meta.status !== "ERROR") {
                    submissionRepo.add(resObj.payload.Submission);
                    defer.resolve(submissionRepo.findById(id));
                }
            });
        } else {
            defer.resolve(submission);
        }
        return defer.promise;
    };

    submissionRepo.query = function (columns, page, size) {
        angular.extend(submissionRepo.mapping.query, {
            'method': 'query/' + page + '/' + size,
            'data': columns
        });
        var promise = WsApi.fetch(submissionRepo.mapping.query);
        promise.then(function (res) {
            if (angular.fromJson(res.body).meta.status !== "ERROR") {
                angular.extend(submissionRepo, angular.fromJson(res.body).payload);
            }
        });
        return promise;
    };

    submissionRepo.batchExport = function (packager) {
        angular.extend(submissionRepo.mapping.batchExport, {
            'method': 'batch-export/' + packager.name
        });
        var promise = FileService.download(submissionRepo.mapping.batchExport);
        return promise;
    };

    submissionRepo.batchUpdateStatus = function (submissionStatus) {
        angular.extend(submissionRepo.mapping.batchUpdateSubmissionStatus, {
            method: "batch-update-status/" + submissionStatus.name
        });
        var promise = WsApi.fetch(submissionRepo.mapping.batchUpdateSubmissionStatus);
        return promise;
    };

    submissionRepo.batchPublish = function (depositLocation) {
        angular.extend(submissionRepo.mapping.batchPublish, {
            method: "batch-publish/" + depositLocation.id
        });
        var promise = WsApi.fetch(submissionRepo.mapping.batchPublish);
        return promise;
    };

    submissionRepo.batchAssignTo = function (assignee) {
        angular.extend(submissionRepo.mapping.batchAssignTo, {
            'data': assignee
        });
        var promise = WsApi.fetch(submissionRepo.mapping.batchAssignTo);
        return promise;
    };

    return submissionRepo;

});
