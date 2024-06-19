'use strict'
const {Storage} = require('@google-cloud/storage');
const fs = require('fs');
// const dateFormat = import('dateformat');
const path = require('path');
const { loadDateFormat } = require('./utils');

const pathKey = path.resolve(__dirname, '../coldswift-d7b99cad60d3.json');

const gcs = new Storage({
    projectId: 'coldswift', 
    keyFilename: pathKey
});

const bucketName = 'coldswift-bucket';
const bucket = gcs.bucket(bucketName);

const folderName = 'event%20image';

function getPublicUrl(filename) {
    return 'https://storage.cloud.google.com/' + bucketName + '/' + folderName + '/' + filename;
};

let ImgUpload = {};

ImgUpload.uploadToGcs = async (req, res, next) => {
    if (!req.file) return next();

    const dateFormat = await loadDateFormat();
    const gcsname = dateFormat(new Date(), "yyyymmdd-HHMMss");
    const file = bucket.file('event image/' + gcsname);

    const stream = file.createWriteStream({
        metadata: {
            contentType: req.file.mimetype
        }
    });

    stream.on('error', (err) => {
        req.file.cloudStorageError = err
        next(err)
    });

    stream.on('finish', () => {
        req.file.cloudStorageObject = gcsname
        req.file.cloudStoragePublicUrl = getPublicUrl(gcsname)
        next()
    });

    stream.end(req.file.buffer);
}

module.exports = ImgUpload;
