# Http Request Creator:

##What is it ?

It's a tool which allows you to create custom HTTP requests, allows easy edition of the URL & its query parameters, allows the addition of custom request headers and request body content, and allows viewing of the response, manipulation (Such as extracting content from HTML with a JQuery-Style CSS selector or a regex,), either as HTML, raw text, or code, a well as saving to file. **Note that it's woefully incomplete, and most stuff does not work yet.**

##What's it for ?

You can:

* Build and test web scrapers, as it lets you download HTML pages and run CSS selectors or regexes on them.

* Test REST APIs

* View the source HTML of webpages, JavaScript, and CSS files - there's a code formatter / prettyprinter built in (based on Google code prettify), as well a deminifier (both not really complete or working yet though)

* Automate things, such as logging into a website and fetching a specific piece of information.

* A ton of other stuff, whatever you can think of :-)

## Features

###What works:

* Edit the URL for your request, add / edit / remove / view query parameters and paths segments, and it does the URL encoding for you.

* Edit the Headers for your outgoing request, add headers (has autocomplete), view & remove.

* View the HTTP headers of the response

* View the response body as plaintext, rendered / formatted HTML, prettified code, and other options.

* Extract text and HTML from the response body text (If it's HTML) using a JQuery style CSS selector (Uses Jsoup under the hood).

###What doesn't work yet and needs to be implemented:

* UI is big, and only suitable for tablets

* Adding request body, at the moment the "Add request body" screen does nothing

* Regex extract and truncate the response body feature

* Some of the other options for viewing / modifying / saving the response.

* Help is missing

* Settings / Options is missing

* Lots of UI controls don't do anything yet.

* Loading webpages and intercepting / viewing assets and ajax requests they make.

* A whole lot of other stuff.

* (Code is somewhat messy and needs some refactoring.)

##Compiling:

Just import the project into Android Studio and hit "Run". Uses Jsoup and OkHttp as dependencies, Android Studio will fetch them automatically.

If you can help improving / completing it, please do !


## Screenshots:
#### Main Screen:
![Main screen](https://raw.githubusercontent.com/JonasCz/HttpTool/master/screenshots/main.png)
***

#### URL & query parameter editor:
![URL editor](https://raw.githubusercontent.com/JonasCz/HttpTool/master/screenshots/url.png)
***

#### Dialog to extract text from the response with a CSS selector:
![CSS extract dialog](https://raw.githubusercontent.com/JonasCz/HttpTool/master/screenshots/cssextract.png)
***

More screenshots can be found in the [screenshots directory](https://github.com/JonasCz/HttpTool/master/screenshots/).

## About

#### Licence is GPLv2+, see the LICENCE file:

**Note:** Don't use or modify my code unless you can comply with the license, and release the source code of your modifications where necessary. I don't want to see my hard work end up in some commercial app. Thanks.

```
This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License along
with this program; if not, write to the Free Software Foundation, Inc.,
51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
```

