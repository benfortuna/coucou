# Background #

Email and groupware clients typically employ a two/three-pane layout with navigation on the left, items (email messages, etc.) on the top right, and an optional reading pane on the bottom right. Navigation is typically hierarchical, primarily based on actions (email, calendar, tasks, etc.), with support for nested contextual folders.

![https://support.ssaihq.com/it/images/stories/itSupport/Applications/Outlook/outlook-folder-list-delegated-mailbox.jpg](https://support.ssaihq.com/it/images/stories/itSupport/Applications/Outlook/outlook-folder-list-delegated-mailbox.jpg)

# Layout #

The layout of Coucou is designed to invert the traditional navigation approach, to focus primarily on context, followed by action. A simple way to visualise this is to imagine having a different Inbox for each context, in the hierarchy, eg. 'Personal/Family', 'Work/Acme Pty Ltd/Timesheets', etc.

http://wiki.coucou.googlecode.com/hg/ccim.PNG

## Action Tabs ##

The primary user interface will consist of a set of _Action Tabs_ that provide functionality for operating within a context. These tabs include:

  * **Inbox** - email communications
  * **Contacts** - extended address book with instant messaging support
  * **Planner** - future events, tasks and appointments
  * **Journal** - a record of past events and outcomes
  * **Notes** - free-form Wiki-style notes
  * **Files** - imported files and attachments

## Reading Tabs ##

Traditional email clients employ a reading pane, or open a new window to show the content of an email, etc. Coucou will make use of a top level tab structure similar to the popular tabbed browsing concept in modern Web browsers.

## Navigator ##

A floatable sidebar window showing a tree view of the folder hierarchy. Folder selection applies a filter such that all Action Tabs only show items in the selected context.

The following special contextual folders are also shown:

  * **Deleted Items** - contains all items deleted from other contexts
  * **Sent Items** - all outgoing communications

## Accounts ##

A list of configured accounts and settings that control how they map to _Contextual Folders_.

## Feeds ##

Site feeds (RSS, Atom, etc.) may be added to any folder to provide additional updates relevant to the context. Items from such feeds are displayed in a floatable demarcated list (i.e. Today, Yesterday, Older Items).

A double-click on a feed item will open a new reading tab showing all items for the selected feed alongside a reading pane.