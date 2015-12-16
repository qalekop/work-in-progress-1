/**
 * Created by akopylov on 23.09.2015.
 */
var gulp = require('gulp');
var react = require('gulp-react');
//var $ = require('gulp-load-plugins')();
//var watchify = require('watchify');
var reactify = require('reactify');
var browserify = require('browserify');
var source = require('vinyl-source-stream');

var sass = require('gulp-sass');

// VARIABLES ======================================================================
var path = {
    JS_SRC: ["components/*.js", "components/**/*.js"],
    JS_DEST: "public/javascripts",
    OUT: "compiled.js",
    ENTRY_POINT: "./main.js",
    SASS_SRC: "public/stylesheets/*.scss",
    SASS_DST: "public/stylesheets"
};

var struct = {
    CSS_DEST: "../scrabble-web/src/main/webapp/styles",
    SCRIPT_DEST: "../scrabble-web/src/main/webapp/js"
};

// TASKS ==========================================================================
// TRANSFORM
gulp.task('transform', function(){
    gulp.src(path.JS_SRC)
        .pipe(react())
        .pipe(gulp.dest(path.JS_DEST));
});

// BUILD
gulp.task('build', function () {
    browserify({
        entries: [path.ENTRY_POINT],
        transform: [[reactify, {"es6": true}]]
    })
        .bundle()
        .pipe(source(path.OUT))
        .pipe(gulp.dest(path.JS_DEST));
});

// STYLES
gulp.task('styles', function() {
    gulp.src(path.SASS_SRC)
        .pipe(sass())
        .pipe(gulp.dest(path.SASS_DST));
});

// COPY
gulp.task('copy-css', function() {
    return gulp.src(path.SASS_DST + "/*.css")
        .pipe(gulp.dest(struct.CSS_DEST));
});

gulp.task('copy-js', function() {
    return gulp.src(path.JS_DEST+ "/" + path.OUT)
        .pipe(gulp.dest(struct.SCRIPT_DEST));
});

// WORKFLOW
gulp.task('make', ['build', 'styles']);
gulp.task('copy', ['copy-css', 'copy-js']);

gulp.task('default', ['make']);
