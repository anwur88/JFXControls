JFXControls
===========

##Show Case Controls (UI Patterns)

<img src="src/site/screenshots/110214.png" alt="Screenshot (11.02.14)" />

###Router (JFXMaps)
[TODO: Doc | Refactor]

###ImageCropper
[TODO: Doc | Refactor]

###MasterDetailsView
* common UI pattern (but also on model layer)
  * master (1:n) detail relation <=> data model (tables)
  * customer 1 : n contract
* vertical OR horizontal mode
* User Experience / Usability Aspects
  * "efficient user experience by allowing the user to stay in the same screen while navigating between items"
  * "see more information for a master list"
* Use Cases: 
  * thumbnail (1) : larger image, download url (n)
  * menu (1) : files, directories, paths (n)
    * i.e. file browser, audio player, contact browser
  * customer (1) : username, address, contracts (n)
  * Tracking the masters selection via properties (<code>IObservableValue</code>)


####JFace



###TreeTableView
[TODO: Impl]

###Further Ideas
* Validator and Decorators (see <a href="http://www.vogella.com/tutorials/EclipseDataBinding/article.html#jfacedb_masterdetail">JFace</a>, <a href="http://www.jidesoft.com/blog/2013/06/06/jidefx-beta-release/">JideFX</a>)
* UI Patterns for WebApps: Search/Results, Spreadsheet, Wizard, Interactive Model (see <a href="http://designingwebinterfaces.com/designing-web-interfaces-12-screen-patterns">Principles and Patterns for Rich Interaction</a>)
* Horizontale Layouts (Java 8)

##Common Tasks

###System Prerequisites
* Java 8 (EA)
* Gradle 1.10
* Git
* some IDE - I'm using eclipse kepler..
* further tools of your like

Pretty soon I'm also going 2 introduce ScalaFX.

###Behind a Proxy?
If you're behind a proxy server: There is an <code>application.conf</code> provided under the following path:

<pre>
JFXControls/src/main/resources/org/devel/jfxcontrols/conf/application.conf
</pre>

Please customize this depending on your needs.

###Eclipsify the Project

<pre>
.. $ gradle eclipse
</pre>

No you're ready 4 import! Remembers me on SBT a bit :D

###Testing
And once again pretty simple:

<pre>
.. $ gradle test
</pre>

By the way, this project uses TestFX 3.0.0 to run automated UI tests on well suited controls ;)

###Build a native client
Just run the following commands havin' a gradle distribution on your env path.

<pre>
.. $ gradle install
.. $ gradle assemble
</pre>

After running the <code>SUCCESSFULL BUILD</code> you'll find the following bundles inside the freshly created <code>build</code> directory:

* executable JAR
* native client 4 windows and linux os
* all required classpath libs to run the sample application
