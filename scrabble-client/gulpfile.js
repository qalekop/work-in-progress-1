/**
 * Created by akopylov on 23.09.2015.
 */
var gulp = require('gulp'),
    react = require('gulp-react');
    stripDebug = require('gulp-strip-debug'),
    uglify = require('gulp-uglify'),
    rename = require('gulp-rename'),
    rm = require( 'gulp-rm' ),
    babel = require('gulp-babel'),
    browserify = require('browserify'),
    source = require('vinyl-source-stream'),
    sass = require('gulp-sass');

// VARIABLES ======================================================================
var path = {
    JS_SRC: ["components/*.js", "components/**/*.js"],
    JS_DEST: "public/javascripts",
    OUT: "compiled.js",
    ENTRY_POINT: "./main.js",
    MINIFIED: "scrabble.min.js",
    SASS_SRC: "public/stylesheets/*.scss",
    SASS_DST: "public/stylesheets"
};

var struct = {
    CSS_DEST: "../scrabble-web/src/main/webapp/styles",
    SCRIPT_DEST: "../scrabble-web/src/main/webapp/js"
};

// TASKS ==========================================================================

gulp.task('clean', function() {
    return gulp.src(path.JS_DEST + '/*', {read: false})
        .pipe(rm());
});

gulp.task('minify', ['build'], function() {
    return gulp.src(path.JS_DEST + '/*.js')
        .pipe(rename(path.MINIFIED))
        .pipe(stripDebug())
        .pipe(uglify())
        .pipe(gulp.dest(path.JS_DEST));
});

// BUILD
gulp.task('build', function () {
    return browserify(path.ENTRY_POINT)
        .transform("babelify", {presets: ["es2015", "react"]})
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

gulp.task('default', ['make', 'copy']);
