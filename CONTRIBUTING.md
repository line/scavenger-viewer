# How to contribute to Scavenger Viewer

First of all, thank you so much for taking your time to contribute! Scavenger-Viewer is not very different from any
other open source projects. It will be fantastic if you help us by doing any of the following:

- File an issue in [the issue tracker](https://github.com/line/scavenger-viewer/issues)
  to report bugs and propose new features and improvements.
- Ask a question using [the issue tracker](https://github.com/line/scavenger-viewer/issues).
- Contribute your work by sending [a pull request](https://github.com/line/scavenger-viewer/pulls).

## Code of Conduct

We expect contributors to follow [our code of conduct](./CODE_OF_CONDUCT.md).

## Contributor license agreement

If you are sending a pull request and it's a non-trivial change beyond fixing
typos, please make sure to sign
the [ICLA (Individual Contributor License Agreement)](https://cla-assistant.io/line/scavenger-viewer).
Please [contact us](mailto:dl_oss_dev@linecorp.com) if you need the CCLA (Corporate Contributor License
Agreement).

## Things to know before you start

### How to execute IDE(installed plugin) in local

* Gradle > intellij > runIde execute
* Run the other intellij IDE with that scavenger-plugin installed.
* When IDE is executed, you can open a project you want to apply or create a new.
* When Project Indexing is completed in IDE, it can be confirmed that the plugin is installed as shown in the image below.

### How to use scavenger-viewer

* If you click Setting Scavenger Button, you should enter scavenger baseurl.
* If you've entered baseurl correctly, you'll see your list of projects in the right tool tree with notification balloon.
* You can check the snapshot list of that project by clicking on the project in the list.

> **_Snapshot Naming Rule:_** {customerName}-{createdAt}-{filterInvokedAtMillis}
If you select the snapshot you want, it will show you the **dead code** according to the result of that snapshot.

## Ways to contribute

### Reporting Bugs

Please report bugs found when using Scavenger-Viewer so we can fix them.
Please report any bugs you find on github issues.

### Suggesting Features

You can also suggest features you'd like to see added to Scavenger-Viewer.
Please report features you would like to see added to github issues.

### Pull Requests

## Developer Guide

### Development languages

* Kotlin 1.9.0 or later

### Build Requirements

* JDK 17 or later

### Intellij IDE Version

* 232(2023.2) ~ 242.*(2024.2.*)

### Add copyright header

* Add the following header to the top of each file.
  ```text
  Copyright $today.year LY Corporation
  LY Corporation licenses this file to you under the Apache License,
  version 2.0 (the "License"); you may not use this file except in compliance
  with the License. You may obtain a copy of the License at:
  https://www.apache.org/licenses/LICENSE-2.0
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
  License for the specific language governing permissions and limitations
  under the License.
  ```

### How to write pull request description

* Motivation
    * Explain why you're sending a pull request and what problem you're trying to solve.
* Modifications
    * List the modifications you've made in detail.
* Result
    * Closes Github issue if this resolves a reported issue.