# Summary #

A plugin is required to implement the following features to integrate with the Coucou Workbench.


## Breadcrumb `PathResult` ##

A _`PathResult`_ implements both the sub-graph of the plugin breadcrumb as well as controlling the results shown in the Activity table.

## Activity Table `ResultLoader` ##

A _`ResultLoader`_ is responsible for initialising the Activity table (e.g. renderer) prior to loading data from the breadcrumb results

## Activity Table `SelectionListener` ##

Handles processing plugin-specific elements of activity selection (e.g. context-sensitive actions, preview display)

## Activity Table `ActivationListener` ##

Optional actions to perform when an activity is activated (i.e. double-clicked). Eg. open link in browser, open new window, etc.

## Ribbon Contextual Actions ##

Present context-specific action UI.