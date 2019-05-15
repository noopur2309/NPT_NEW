function IlaInsertion() {
  this.numOfIla = 0;
  this.spanLoss = 0;
  this.spanLength = 0;
  this.linkProps = null;
  this.linkModel = null;
  this.linkAdd_NodeConfig = null;
  this.ilaType = nptGlobals.getDefaultLinkStr();
  this.userChoice = null;
  this.linkType = nptGlobals.getDefaultLinkStr();
  this.spanLengthInputFieldsIndexSet = new Set();
  this.spanLossInputFieldsIndexSet = new Set();
  this.SpanlengthArray = [];
  this.choiceOne = "Option 1";
  this.choiceTwo = "Option 2";
  this.choiceThree = "Option 3";
  this.choiceFour = "Option 4";

  this.insertIla = () => {
    "use strict";
    // console.log("Inside insertIla", this);
    let spanloss = this.spanLoss;

    this.numOfIla = 1;
    if (this.userChoice == this.choiceOne) {
      this.ilaType = this.linkType = nptGlobals.getDefaultLinkStr();
      if (spanloss > nptGlobals.getDefaultLinkMaxSpanLoss()) {
        this.numOfIla = Math.floor(
          spanloss / nptGlobals.getDefaultLinkMaxSpanLoss()
        );
      }
    } else if (this.userChoice == this.choiceTwo) {
      this.ilaType = this.linkType = nptGlobals.getRamanHybridLinkStr();
      if (spanloss > nptGlobals.getRamanHybridMaxSpanloss()) {
        this.numOfIla = Math.floor(
          spanloss / nptGlobals.getRamanHybridMaxSpanloss()
        );
      }
    } else if (this.userChoice == this.choiceThree) {
      this.ilaType = this.linkType = nptGlobals.getRamanDraLinkStr();
      if (spanloss > nptGlobals.getRamanDraMaxSpanloss()) {
        this.numOfIla = Math.floor(
          spanloss / nptGlobals.getRamanDraMaxSpanloss()
        );
      }
    } else if (this.userChoice == this.choiceFour) {
      this.numOfIla = Math.floor(
        spanloss / nptGlobals.getMaxSpanLoss(this.linkType)
      );
      if (this.numOfIla == 0) {
        this.numOfIla = 1;
      }
    }

    this.getSpanlengthforIlaLinksFromUser();
  };

  this.validateIlaInsertion = (link, linkUserInput) => {
    "use strict";
    // debugger;
    this.linkProps = JSON.parse(JSON.stringify(linkUserInput));

    this.spanLength = this.linkProps.spanLength;
    this.spanLoss = this.linkProps.calculatedSpanLoss;
    this.linkType = this.linkProps.linkType;
    this.linkModel = link;
    // debugger;

    // If ILA is inserted using node-link config tab
    if (linkUserInput.hasOwnProperty("updateLinkConfigTab"))
      this.linkAdd_NodeConfig = true;
    else this.linkAdd_NodeConfig = false;

    var messageAlert = "",
      titleText = `<p class="text-center lead">ILA insertion dialog</p>`;

    messageAlert += `Span loss this link is <strong><mark>${
      this.spanLoss
    } dB</mark></strong>. <p>`;

    messageAlert += `<br><br> <strong>Option 1 :</strong> Automatically add <mark>ILA(PA/BA)</mark> between the nodes.`;
    messageAlert += `<br><br> <strong>Option 2 :</strong> Automatically add <mark>ILA (RAMAN HYBRID)</mark> between the nodes.`;
    messageAlert += `<br><br> <strong>Option 3 :</strong> Automatically add <mark>ILA (RAMAN DRA)</mark> between the nodes.`;

    messageAlert += `<br><br> <strong>Option 4 :</strong> If you want to specifically design the link as per your requirements,
		 then you need to provide Spanlength values for all links alongwith ILA type in the next screen.`;

    messageAlert += `<br><br>Press <span class="text-danger">Cancel</span>
		to adjust the link parameters . <br> Choose  <strong class="text-success">Any Option </strong> to add amplifier on this link or at source/destination nodes.`;

    var that = this;
    bootbox.dialog({
      message: messageAlert,
      title: titleText,
      buttons: {
        "Option 1": {
          className: "btn btn--default",
          callback: function() {
            console.log("This inside modal :", that);
            that.userChoice = that.choiceOne;
            that.insertIla();
          }
        },
        "Option 2": {
          className: "btn btn--default",
          callback: function() {
            that.userChoice = that.choiceTwo;
            that.insertIla();
          }
        },
        "Option 3": {
          className: "btn btn--default",
          callback: function() {
            that.userChoice = that.choiceThree;
            that.insertIla();
          }
        },
        "Option 4": {
          className: "btn btn--default",
          callback: function() {
            that.userChoice = that.choiceFour;
            that.insertIla();
          }
        },
        Cancel: {
          className: "btn btn--default",
          callback: function() {}
        }
      }
    });
  };

  this.getSpanlengthforIlaLinksFromUser = () => {
    "use strict";
    // console.log("getSpanlengthforIlaLinksFromUser", this);
    // debugger;
    let ilaCount = this.numOfIla;
    let numOfLinks = ilaCount + 1;

    var spanLengthInitial = Math.round(this.spanLength / numOfLinks),
      spanLossInitial = Math.round(this.spanLoss / numOfLinks),
      spanLengthRemaining = this.spanLength,
      spanLossRemaining = this.spanLoss;

    let offsetSpanlength = nptGlobals.getMaxIlaSpanLength();
    let offsetSpanloss = nptGlobals.getMaxSpanLoss(this.linkType);
    if (this.linkType == nptGlobals.getDefaultLinkStr()) {
      offsetSpanlength = 120;
    } else if (this.linkType == nptGlobals.getRamanHybridLinkStr()) {
      offsetSpanlength = 126;
    } else {
      offsetSpanlength = 40;
    }
    spanLengthInitial =
      spanLengthInitial > offsetSpanlength
        ? spanLengthInitial - 1
        : spanLengthInitial;
    spanLossInitial =
      spanLossInitial > offsetSpanloss ? spanLossInitial - 1 : spanLossInitial;

    let obj;
    if (
      this.userChoice == this.choiceOne ||
      this.userChoice == this.choiceTwo ||
      this.userChoice == this.choiceThree
    ) {
      // Serialize initial (form) data into its object
      let arr = [];
      let j = 0;
      for (var i = 0; i < ilaCount * 2; i += 2) {
        obj = {};
        obj["name"] = `spanLength_${j}`;
        obj["value"] = spanLengthInitial;
        arr.push(obj);

        obj = {};
        obj["name"] = `spanLoss_${j++}`;
        obj["value"] = spanLossInitial;
        arr.push(obj);

        spanLengthRemaining -= spanLengthInitial;
        spanLossRemaining -= spanLossInitial;
        console.log(
          "spanLengthRemaining",
          spanLengthRemaining,
          "spanLossRemaining",
          spanLossRemaining
        );
      }

      obj = {};
      obj["value"] = Math.round(spanLengthRemaining * 100) / 100;
      obj["name"] = `spanLength_${j}`;
      arr.push(obj);

      obj = {};
      obj["name"] = `spanLoss_${j}`;
      obj["value"] = Math.round(spanLossRemaining * 100) / 100;
      arr.push(obj);

      console.log("Array of Automatic Spanlengths ::", arr);

      // dataObjectIlaInsertion.SpanlengthArray = arr;
      this.SpanlengthArray = arr;
      // console.log("getSpanlengthforIlaLinksFromUser", this);
      // debugger;
      this.fIlaLinksInputFormSubmit();
    } else if (this.userChoice == this.choiceFour) {
      var title = ` <p class="lead text-center text-primary"><strong>Set spanlength Values for links</strong></p>
    <hr>
    <div class="row">
      <div class="col-md-12 text-center">
        <p><small>AMPLIFIER TYPE : </small><strong>${
          this.ilaType
        }</strong></p>  
      </div>
	  </div>`;

      let template = `<div id="ilaLinksInputDiv">`;

      template += this.fGetManualIlaInsertionTemplate();
      template += `</div>`;

      bootbox.dialog({
        message: template,
        title: title,
        buttons: {
          Back: {
            className: "btn btn--default",
            callback: function() {
              this.validateIlaInsertion(this.linkModel, this.linkProps);
            }
          },
          "Reset to Default": {
            className: "btn btn--default",
            callback: function() {
              this.getSpanlengthforIlaLinksFromUser();
            }
          },
          "Insert Ila": {
            className: "btn btn--default",
            callback: function() {
              console.log("This inside modal :", this);
              this.fIlaLinksInputFormSubmit();
            }
          },
          Cancel: {
            className: "btn btn--default",
            callback: function() {}
          }
        }
      });

      this.spanLengthInputFieldsIndexSet.clear();
      this.spanLossInputFieldsIndexSet.clear();
      this.updateSpanLossInManualIlaInsertion();
    }
  };

  this.InsertIlaBetweenNodes = userInputForIlaLinksArray => {
    "use strict";
    // console.log("InsertIlaBetweenNodes", this);
    // debugger;

    // let linkModel = nptGlobals.;
    let link = this.linkModel;
    var ilaLinksArr = [];

    let srcNodeId = link.source().id,
      destNodeId = link.target().id;

    let srcNode = graph.getCell(srcNodeId),
      destNode = graph.getCell(destNodeId);

    //Link removal will also update the node connections
    //To preserve the previous connections on link removal
    let srcInitialDir = link.source().dir,
      destInitialDir = link.target().dir;

    // srcInitialNodeConnections = JSON.parse(
    //   JSON.stringify(srcNode.get("nodeProperties").nodeConnections)
    // ),
    // destInitialNodeConnections = JSON.parse(
    //   JSON.stringify(destNode.get("nodeProperties").nodeConnections)
    // );

    console.log(
      "srcInitialDir:",
      srcInitialDir,
      " destInitialDir:",
      destInitialDir
    );
    // console.log("Src Node Initial Connections", srcInitialNodeConnections);
    // console.log("Dest Node Initial Connections", destInitialNodeConnections);

    console.log("Original Link before ILA insertion", link);

    console.log("srcNodeId :", srcNodeId, " destNodeId:", destNodeId);
    //var isReversedLink=0;

    var srcx, srcy, destx, desty, xDistance;
    srcx = graph.getCell(srcNodeId).get("position").x;
    srcy = graph.getCell(srcNodeId).get("position").y;
    destx = graph.getCell(destNodeId).get("position").x;
    desty = graph.getCell(destNodeId).get("position").y;
    xDistance = eval("destx - srcx");
    console.log("xDistance ::" + xDistance);

    var userInputForIlaLinks = userInputForIlaLinksArray.filter(el => {
      return el.name.includes("spanLength");
    });

    console.log(
      "*********** userInputForIlaLinks Length **********",
      userInputForIlaLinks.length,
      userInputForIlaLinks
    );
    ilaLinksArr.push(link.id);

    //	let linkSubType=linkModel.get('linkProperties').linkType;
    // let linkSubType = dataObjectIlaInsertion.ilaType;

    for (var i = 0; i < userInputForIlaLinks.length - 1; i++) {
      ilaLinksArr.pop();

      console.log("numOfIla", Number(i + 1));

      srcNodeId = link.get("source").id;
      destNodeId = link.get("target").id;

      var p = {};
      let spanLengthSrcDest = this.spanLength;
      var percentage = eval("userInputForIlaLinks[i].value/spanLengthSrcDest");
      //console.log("percentage ::"+percentage)
      var percentageLength = eval("xDistance * percentage");
      //console.log("percentageLength ::"+percentageLength)
      p.x = eval("srcx + percentageLength");
      srcx = p.x;
      //console.log("value of X coordinate ::"+p.x);
      p.y = (srcy + desty) / 2;

      //console.log("X and Y Coordinates --->"+p.x +"  "+p.y);
      var ila = insertIlaNode(p);
      ila.attributes.nodeProperties.nodeSubtype = this.ilaType;
      console.log(
        "Link type ::",
        link.get("linkProperties").linkType,
        " Subtype of Ila::",
        ila.get("nodeProperties").nodeSubtype
      );

      // var deletedLink = link;
      // if (i == 0) link.attributes.linkProperties.autoDelete = true;
      link.target({ id: ila.id });
      // debugger;
      // link.remove();
      // delay(2000);
      var firstLink = link; //insertLink(srcNodeId, ila.id);
      // debugger;
      // firstLink.attributes.linkProperties.linkType =
      //   dataObjectIlaInsertion.ilaType;

      var firstLinkSrcNode = firstLink.getSourceElement();
      var firstLinkDestNode = firstLink.getTargetElement();
      console.log("firstLinkSrcNode :", firstLinkSrcNode);

      // Update new connection link properties(Src and Dest Node Dir)
      // fUpdateLinkConnectionProperties(firstLink);

      /******  Keep the direction of link same for source node  *****/
      if (i == 0) {
        firstLink.get("target").dir = paperUtil.fGetNodeDegree(
          firstLinkDestNode
        );
        firstLink.get("target").NodeId = firstLinkDestNode.get(
          "nodeProperties"
        ).nodeId;
      } else {
        firstLink.get("target").dir = paperUtil.fGetNodeDegree(
          firstLinkDestNode
        );
        firstLink.get("target").NodeId = firstLinkDestNode.get(
          "nodeProperties"
        ).nodeId;

        firstLink.get("source").dir = paperUtil.fGetNodeDegree(
          firstLinkSrcNode
        );
        firstLink.get("source").NodeId = firstLinkSrcNode.get(
          "nodeProperties"
        ).nodeId;
      }

      // firstLink.get("linkProperties").linkType = this.linkType;
      ilaLinksArr.push(firstLink.id);

      console.log("Loop :", i, " firstLink::", firstLink);
      console.log("this before calling", this.linkProps);
      this.setNewLinkPropertiesWithIla(firstLink, userInputForIlaLinksArray, i);

      var secondLink = insertLink(ila.id, destNodeId);

      var secondLinkSrcNode = secondLink.getSourceElement();
      var secondLinkDestNode = secondLink.getTargetElement();

      // Update new connection link properties (Src and Dest Node Dir)
      // fUpdateLinkConnectionProperties(secondLink);

      secondLink.get("source").dir = paperUtil.fGetNodeDegree(
        secondLinkSrcNode
      );
      secondLink.get("source").NodeId = secondLinkSrcNode.get(
        "nodeProperties"
      ).nodeId;

      /******  Keep the direction of link same for dest node  *****/
      if (i == userInputForIlaLinks.length - 2) {
        secondLink.get("target").dir = destInitialDir;
      } else
        secondLink.get("target").dir = paperUtil.fGetNodeDegree(
          secondLinkDestNode
        );

      secondLink.get("target").NodeId = secondLinkDestNode.get(
        "nodeProperties"
      ).nodeId;

      // secondLink.get("linkProperties").linkType = this.linkType;
      ilaLinksArr.push(secondLink.id);

      console.log("Loop :", i, " secondLink::", secondLink);
      console.log(
        "secondLinkSrcNode::",
        secondLinkSrcNode.attributes.nodeProperties.nodeConnections
      );
      console.log(
        "secondLinkDestNode::",
        secondLinkDestNode.attributes.nodeProperties.nodeConnections
      );

      this.setNewLinkPropertiesWithIla(
        secondLink,
        userInputForIlaLinksArray,
        i + 1
      );

      //Ila insertion in case of Line Protected link
      var isLineProtected = this.linkProps.lineProtection;
      if (isLineProtected == 1) {
        //firstLink.attr(attrs.linkDefaultLinePtc);
        firstLink.get("linkProperties").lineProtection = 1;
        //secondLink.attr(attrs.linkDefaultLinePtc);
        secondLink.get("linkProperties").lineProtection = 1;
      }

      link = secondLink;
      console.log("Second Link link::", link);
    }

    console.log("ilaLinksArr", ilaLinksArr);

    setallNodeAttributes();
    // setallLinkAttributes();
    this.setAllIlaLinksProperties(ilaLinksArr);

    if (this.linkAdd_NodeConfig) {
      updateLinkNodeConfigTab();
    }
  };

  this.updateSpanLossInManualIlaInsertion = () => {
    var spanLengthLinks = $(".spanLengthIlaLinks").toArray();
    var spanLossLinks = $(".spanLossIlaLinks").toArray();
    console.log("spanLossLinks ::", spanLossLinks);
    console.log("Ila User Input Link", this.linkProps);
    let originalLinkProperties = this.linkProps;
    spanLengthLinks.map(function(spanLengthInput, index) {
      console.log(
        "$spanLengthInput.val()",
        $(spanLengthInput).val(),
        " index",
        index
      );
      $(spanLossLinks[index]).val(
        eval(
          originalLinkProperties.coefficient * $(spanLengthInput).val() +
            originalLinkProperties.splice *
              originalLinkProperties.lossPerSplice +
            originalLinkProperties.connector *
              originalLinkProperties.lossPerConnector
        ).toFixed(2)
      );
    });
  };

  this.fIlaLinksInputFormSubmit = () => {
    var userInputForIlaLinks,
      spanLengthErroFlag = false;
    // console.log("fIlaLinksInputFormSubmit", this);
    // debugger;
    if (
      this.userChoice == this.choiceOne ||
      this.userChoice == this.choiceTwo ||
      this.userChoice == this.choiceThree
    ) {
      userInputForIlaLinks = this.SpanlengthArray;
    } else userInputForIlaLinks = $("#ilaLinksInputForm").serializeArray();

    console.log("userInputForIlaLinks", userInputForIlaLinks);

    var userInputlengthSum = 0;
    var userInputlossSum = 0;
    console.log("userInputForIlaLinks.length ::", userInputForIlaLinks.length);
    let individualSpanlengthMaxLimit = 120;
    let individualSpanlengthMinLimit = 10;

    if (this.userChoice == this.choiceTwo) {
      individualSpanlengthMinLimit = 10;
      individualSpanlengthMaxLimit = 127;
    } else if (this.userChoice == this.choiceThree) {
      individualSpanlengthMinLimit = 10;
      individualSpanlengthMaxLimit = 40;
    }

    let linkSpanLenthError = "";
    for (var i = 0; i < userInputForIlaLinks.length; i++) {
      let spanLength = Number(userInputForIlaLinks[i++].value),
        spanLoss = Number(userInputForIlaLinks[i].value);
      console.log("spanLoss value from array:", spanLoss);
      console.log("spanLength value from array:", spanLength);

      // Individual Span Validation
      if (
        spanLength < individualSpanlengthMinLimit ||
        spanLength > individualSpanlengthMaxLimit
      ) {
        spanLengthErroFlag = true;
        linkSpanLenthError = `Individual Span Length
			should be greater than <strong>${individualSpanlengthMinLimit}</strong> and less than 
			<strong>${individualSpanlengthMaxLimit}</strong>`;
        break;
      }
      //		 console.log(spanLength);
      userInputlengthSum = Number(userInputlengthSum) + spanLength;
      //		 console.log(spanLoss);
      userInputlossSum = Number(userInputlossSum) + spanLoss;
    }

    console.log("userInputlengthSum Value ::", userInputlengthSum);
    console.log("original spanLength Value ::", this.spanLength);
    console.log("userInputlossSum Value ::", userInputlossSum);
    console.log("original SPANLOSS Value ::", this.spanLoss);

    userInputlossSum = Math.round(userInputlossSum * 100) / 100;
    console.log(
      "After rounding off - userInputlossSum Value ::" + userInputlossSum
    );

    //Check if spanlength values are in range
    if (spanLengthErroFlag) {
      // console.log(userInputForIlaLinks[0].value, " :: ", userInputForIlaLinks[2].value)
      var template = `Individual Span Length
				should be greater than <strong>10</strong> and less than <strong>120</strong>`;
      // var title = `<p class="text-danger">Invalid Input</p>`;

      bootBoxDangerAlert(template);
      this.getSpanlengthforIlaLinksFromUser();
    } else if (userInputlengthSum == this.spanLength) {
      console.log("Insert ILA's");
      bootBoxDangerAlert("Inserting Ila between nodes");
      this.InsertIlaBetweenNodes(userInputForIlaLinks);
    } else {
      var template = `Total span-length
			should be equal to <strong>${this.spanLength}</strong>`;
      // var title = `<p class="text-danger">Invalid Input</p>`;

      bootBoxDangerAlert(template);
      this.getSpanlengthforIlaLinksFromUser();
    }
  };

  this.setNewLinkPropertiesWithIla = (
    linkModel,
    userInputForIlaLinksArray,
    i
  ) => {
    console.log(
      "setNewLinkPropertiesWithIla inside -> ",
      this,
      this.linkProps.fiberType,
      this.linkProps.costMetric
    );
    console.log(
      "setNewLinkPropertiesWithIla linkModel ::",
      linkModel.get("linkProperties").linkId
    );

    // console.log(
    //   "userInputForIlaLinksArray[(i*2)].value ::" +
    //     userInputForIlaLinksArray[i * 2].value
    // );
    // console.log(
    //   "userInputForIlaLinksArray[((i*2)+1)].value; ::" +
    //     userInputForIlaLinksArray[i * 2 + 1].value
    // );

    linkModel.get("linkProperties").spanLength =
      userInputForIlaLinksArray[i * 2].value;
    linkModel.get("linkProperties").calculatedSpanLoss =
      userInputForIlaLinksArray[i * 2 + 1].value;
    linkModel.get("linkProperties").adjustableSpanLoss =
      userInputForIlaLinksArray[i * 2 + 1].value;
    // linkModel.get("linkProperties").fiberType = this.linkProps.fiberType;
    linkModel.get(
      "linkProperties"
    ).lossPerSplice = this.linkProps.lossPerSplice;
    linkModel.get("linkProperties").splice = this.linkProps.splice;
    linkModel.get("linkProperties").connector = this.linkProps.connector;
    linkModel.get(
      "linkProperties"
    ).lossPerConnector = this.linkProps.lossPerConnector;
    // linkModel.get("linkProperties").costMetric = this.linkProps.costMetric;
    // linkModel.get("linkProperties").srlg = this.linkProps.srlg;
    linkModel.get("linkProperties").color = this.linkProps.color;
    linkModel.get(
      "linkProperties"
    ).opticalParameter = this.linkProps.opticalParameter;

    console.log("LinkModel ::", linkModel);
  };

  this.fGetManualIlaInsertionTemplate = () => {
    let numOfIla = this.numOfIla;
    let numOfLinks = numOfIla + 1;
    let linkModel = this.linkModel;
    var srcNodeId = linkModel.get("source").NodeId;
    var destNodeId = linkModel.get("target").NodeId;

    var spanLengthInitial = Math.round(this.spanLength / numOfLinks),
      spanLossInitial = Math.round(this.spanLoss / numOfLinks),
      spanLengthRemaining = this.spanLength,
      spanLossRemaining = this.spanLoss;

    let offsetSpanLength,
      offsetSpanloss = nptGlobals.getMaxSpanLoss(this.linkType);
    if (this.linkType == nptGlobals.getDefaultLinkStr()) {
      offsetSpanlength = 120;
    } else if (this.linkType == nptGlobals.getRamanHybridLinkStr()) {
      offsetSpanlength = 126;
    } else {
      offsetSpanlength = 40;
    }
    spanLengthInitial =
      spanLengthInitial > offsetSpanLength
        ? spanLengthInitial - 1
        : spanLengthInitial;
    spanLossInitial =
      spanLossInitial > offsetSpanloss ? spanLossInitial - 1 : spanLossInitial;

    let template = `<form id="ilaLinksInputForm">
          
          <div class="form-group first" >
          <p class="text-center strong"><strong>Node ${srcNodeId} to ILA 1</strong></p>
          <div class="row">
          <div class="col-md-6 col-sm-6">
          <label for="spanlength">Spanlength (kms)</label>
          <input  type="number" min="1" max="640" step="1" class="form-control spanLengthIlaLinks" placeholder="40"  value="${spanLengthInitial}" name="spanLength_0">
          </div>
          <div class="col-md-6 col-sm-6">
          <label for="spanloss">Spanloss (db)</label>
          <input  type="number" min="1" max="35" step="1" class="form-control spanLossIlaLinks" placeholder="40"  value="${spanLossInitial}" name="spanLoss_0" readonly>
          </div>	</div>	
      </div>`;

    spanLengthRemaining = spanLengthRemaining - spanLengthInitial;
    spanLossRemaining = spanLossRemaining - spanLossInitial;

    for (var i = 1; i < numOfIla; i++) {
      template += `<div class="form-group" >
          <p class="text-center"><strong>ILA ${i} to ILA ${i + 1}</strong></p>
          <div class="row">
          <div class="col-md-6 col-sm-6">
          <label for="spanlength">Spanlength (kms)</label>
          <input  type="number" min="1" max="640" step="1" class="form-control spanLengthIlaLinks" placeholder="${spanLengthInitial}"  value="${spanLengthInitial}" name="spanLength_${i}">
          </div>
          <div class="col-md-6 col-sm-6">
          <label for="spanloss">Spanloss (db)</label>
          <input  type="number" min="1" max="35" step="1" class="form-control spanLossIlaLinks" placeholder="${spanLossInitial}"  value="${spanLossInitial}" name="spanLoss_${i}" readonly>
          </div>		</div>
      </div>`;
      spanLengthRemaining = spanLengthRemaining - spanLengthInitial;
      spanLossRemaining = spanLossRemaining - spanLossInitial;
    }

    template += `<div class="form-group" >
          <p class="text-center"><strong>ILA ${i} to Node ${destNodeId}</strong></p>
          <div class="row">
          <div class="col-md-6 col-sm-6">
          <label for="spanlength">Spanlength (kms)</label>
          <input  type="number" min="1" max="640" step="1" class="form-control spanLengthIlaLinks" placeholder="${spanLengthRemaining}"  value="${spanLengthRemaining}" name="spanLength_${i}">
          </div>
          <div class="col-md-6 col-sm-6">
          <label for="spanloss">Spanloss (db)</label>
          <input  type="number" min="1" max="35" step="1" class="form-control spanLossIlaLinks" placeholder="${spanLossRemaining}"  value="${spanLossRemaining}" name="spanLoss_${i}" readonly>
          </div>	</div>	
      </div>
  
      </form>`;
    return template;
  };

  this.setAllIlaLinksProperties = ilaLinksArr => {
    for (var i = 0; i < ilaLinksArr.length; i++) {
      let link = graph.getCell(ilaLinksArr[i]);
      console.log("setAllIlaLinksProperties:", link);
      // console.log("Color for Ila Link :" + this.linkProps.color, " i :", i);
      setLinkColor(link, this.linkProps.color);
      //set attributes of object/element/link properties defined

      // link.attributes.linkProperties.fiberType = this.linkProps.fiberType;

      if (link.attributes.linkProperties.fiberType == 1) {
        /**G.652.D*/
        console.log("pmdCoff :" + this.linkProps.pmdCoefficient);
        console.log("CdCoff :" + this.linkProps.cdCoefficient);
        console.log("New SpanLength :" + link.get("linkProperties").spanLength);
        var cdValue = eval(
          this.linkProps.cdCoefficient * link.get("linkProperties").spanLength
        ).toFixed(2);
        var pmdValue = eval(
          this.linkProps.pmdCoefficient *
            Math.sqrt(link.get("linkProperties").spanLength)
        ).toFixed(2);
        console.log("cdValue : " + cdValue + " and pmdValue : " + pmdValue);
        link.attributes.linkProperties.pmd = pmdValue;
        link.attributes.linkProperties.cd = cdValue;
      } else if (link.attributes.linkProperties.fiberType == 2) {
        /**G.655*/
        console.log("pmdCoff :" + this.linkProps.pmdCoefficient);
        console.log("SpanLength :" + nptGlobals.IlaInsertion_SpanLength);
        console.log("New SpanLength :" + link.get("linkProperties").spanLength);
        var cdValue = eval(
          this.linkProps.cdCoefficient * link.get("linkProperties").spanLength
        ).toFixed(2);
        var pmdValue = eval(
          this.linkProps.pmdCoefficient *
            Math.sqrt(link.get("linkProperties").spanLength)
        ).toFixed(2);
        console.log("cdValue : " + cdValue + " and pmdValue : " + pmdValue);
        link.attributes.linkProperties.pmd = pmdValue;
        link.attributes.linkProperties.cd = cdValue;
      }

      //link.attributes.linkProperties.splice = this.linkProps.splice;
      //link.attributes.linkProperties.lossPerSplice = this.linkProps.lossPerSplice;
      //link.attributes.linkProperties.connector = this.linkProps.connector;
      //link.attributes.linkProperties.lossPerConnector = this.linkProps.lossPerConnector;
      //link.attributes.linkProperties.calculatedSpanLoss = this.linkProps.calculatedSpanLoss;
      //link.attributes.linkProperties.adjustableSpanLoss = this.linkProps.adjustableSpanLoss;
      // link.attributes.linkProperties.costMetric = this.linkProps.costMetric;
      //link.attributes.linkProperties.opticalParameter = this.linkProps.opticalparameter;
      link.attributes.linkProperties.srlg = this.linkProps.srlg;
      link.attributes.linkProperties.color = this.linkProps.color;
      //cell.attr('attributes/stationName',stationName);

      let prevLinkType = link.get("linkProperties").linkType;
      // link.get("linkProperties").linkType = this.linkType;
      if (
        this.linkType != nptGlobals.getDefaultLinkStr() &&
        prevLinkType == nptGlobals.getDefaultLinkStr()
      ) {
        console.log(
          "************** Link type has changed from ",
          prevLinkType,
          " to ",
          this.linkType
        );
        link.get("linkProperties").linkType = this.linkType;
        convertLinkType(link);
      }
      //Update link Info Tab
      linkInfoTab();
    }
    setallLinkAttributes();
  };
}

var IlaModule = new IlaInsertion();

// events

$("body").delegate(".spanLengthIlaLinks", "change", function(evt) {
  console.log(".spanLengthIlaLinks");
  let val = $(this).val();
  console.log("Changed Val:", val);
  let spanLengthInitial = 40;
  let spanLengthSum = IlaModule.spanLength;

  if (
    val > nptGlobals.getMaxIlaSpanLength() ||
    val < nptGlobals.getMinIlaSpanLength()
  ) {
    bootBoxAlert(
      "Spanlength cannot be greater than " +
        nptGlobals.getMaxIlaSpanLength() +
        " and less than " +
        nptGlobals.getMinIlaSpanLength() +
        " for a link"
    );
    if (val > nptGlobals.getMaxIlaSpanLength())
      $(this).val(nptGlobals.getMaxIlaSpanLength());
    else $(this).val(nptGlobals.getMinIlaSpanLength());
  }

  let spanLengthInputs = $(".spanLengthIlaLinks");
  console.log("spanLengthInputs:", spanLengthInputs);

  let indexOfSpanLengthInput = $(this)
    .attr("name")
    .split("_")[1];
  console.log("Index:" + indexOfSpanLengthInput);
  IlaModule.spanLengthInputFieldsIndexSet.add(indexOfSpanLengthInput);
  console.log("Before", IlaModule.spanLengthInputFieldsIndexSet);
  console.log(
    "size equal :",
    spanLengthInputs.length == IlaModule.spanLengthInputFieldsIndexSet.size
  );
  if (spanLengthInputs.length == IlaModule.spanLengthInputFieldsIndexSet.size) {
    console.log("Clearing Set");
    IlaModule.spanLengthInputFieldsIndexSet.clear();
    IlaModule.spanLengthInputFieldsIndexSet.add(indexOfSpanLengthInput);
  }
  console.log("After", IlaModule.spanLengthInputFieldsIndexSet);

  let numOfChangeFields = 0,
    changeFieldsArr = [],
    noChangeFieldsSum = 0;

  // Span through all inputs for spanLength and then find the inputs which are supposed to
  // see the change (Not in set containing already changed value so that we do not change them again)
  spanLengthInputs.each(function(index) {
    console.log("Element Index :" + index + " Value :" + $(this).val());
    console.log(IlaModule.spanLengthInputFieldsIndexSet);
    console.log(
      "Set Contents -- " +
        IlaModule.spanLengthInputFieldsIndexSet.has($.trim(index))
    );

    if (IlaModule.spanLengthInputFieldsIndexSet.has($.trim(index))) {
      console.log("Already in Set");
      noChangeFieldsSum += Number($(this).val()); // NO change in this field
    } else {
      console.log("Not present in set");
      changeFieldsArr.push({ Index: index, Value: $(this) });
      numOfChangeFields++;
      //$(this).val(10);
    }
  });

  console.log("numOfChangeFields:" + numOfChangeFields);
  console.log("noChangeFieldsSum:" + noChangeFieldsSum);
  console.log("spanLengthSum:" + spanLengthSum);
  console.log("spanLengthInputs.length:" + spanLengthInputs.length);
  console.log(changeFieldsArr);

  var changeFieldsValue = eval(
    (spanLengthSum - noChangeFieldsSum) / numOfChangeFields
  );
  console.log("changeFieldsValue", changeFieldsValue);
  var prevValue = spanLengthSum - changeFieldsValue * changeFieldsArr.length;
  console.log("previous value :", prevValue);

  if (changeFieldsValue < nptGlobals.getMinIlaSpanLength()) {
    changeFieldsValue = nptGlobals.getMinIlaSpanLength();
    $(this).val(spanLengthSum - changeFieldsValue * changeFieldsArr.length);
  } else if (changeFieldsValue > nptGlobals.getMaxIlaSpanLength()) {
    changeFieldsValue = nptGlobals.getMaxIlaSpanLength();
    $(this).val(spanLengthSum - changeFieldsValue * changeFieldsArr.length);
  }

  for (var i in changeFieldsArr) {
    console.log(changeFieldsArr[i]);
    changeFieldsArr[i].Value.val(changeFieldsValue);
    /*if(changeFieldsArr[i].Value.val()==0)	
           {
            console.log("Adding to set as this input has value 0");
            IlaModule.spanLengthInputFieldsIndexSet.add(changeFieldsArr[i].Index )
           }*/
  }

  updateSpanLossInManualIlaInsertion();
});

// $("body").delegate(".spanLossIlaLinks", "input", function() {
//   console.log(".spanLengthIlaLinks");
//   var val = $(this).val();
//   var spanLossLimit = 35;
//   var spanLossInitial = 10;
//   var finalSpanLoss = val;
//   var spanLossSum = nptGlobals.IlaInsertion_SpanLoss;

//   console.log("Input value Spnaloss::" + val);
//   if (val > spanLossLimit) {
//     bootBoxAlert(
//       "Spanloss cannot be greater than " + spanLossLimit + " for a link"
//     );
//     //closeBootBoxAlert();
//     $(this).val(spanLossInitial);
//     finalSpanLoss = spanLossLimit;
//   } else {
//     var indexOfSpanLossInput = $(this)
//       .attr("name")
//       .split("_")[1];
//     console.log("Index:" + indexOfSpanLossInput);
//     spanLossInputFieldsIndexSet.add(indexOfSpanLossInput);

//     var spanLossInputs = $(".spanLossIlaLinks");
//     var numOfChangeFields = 0,
//       changeFieldsArr = [],
//       noChangeFieldsSum = 0;

//     // Span through all inputs for spanLoss and then find the inputs which are supposed to
//     // see the change (Not in set containing already changed value so that we do not change them again)
//     spanLossInputs.each(function(index) {
//       console.log("Element Index :" + index + " Value :" + $(this).val());
//       console.log(spanLossInputFieldsIndexSet);
//       console.log(
//         "Set me hai kYA? -- " + spanLossInputFieldsIndexSet.has($.trim(index))
//       );

//       if (spanLossInputFieldsIndexSet.has($.trim(index))) {
//         console.log("Ye wala pehle se hai");
//         noChangeFieldsSum += Number($(this).val()); // NO change fields
//       } else {
//         console.log("Ye wala pehle se nahi hai");
//         changeFieldsArr.push({ Index: index, Value: $(this) });
//         numOfChangeFields++;
//         //$(this).val(10);
//       }
//     });

//     console.log("numOfChangeFields:" + numOfChangeFields);
//     console.log("noChangeFieldsSum:" + noChangeFieldsSum);
//     console.log("spanLossSum:" + spanLossSum);
//     console.log("spanLossInputs.length:" + spanLossInputs.length);
//     console.log(changeFieldsArr);

//     var changeFieldsValue = eval(
//       (spanLossSum - noChangeFieldsSum) / numOfChangeFields
//     );

//     for (let el of changeFieldsArr) {
//       console.log(el);
//       el.Value.val(changeFieldsValue);
//     }

//     if (spanLossInputs.length == spanLossInputFieldsIndexSet.size) {
//       console.log("Clearing SpanLoss Set");
//       spanLossInputFieldsIndexSet.clear();
//     }
//   }
// });

// $("body").delegate("#ilaType", "change", function() {
//   let ilaType = $(this).val(),
//     numOfIla = 1,
//     spanLoss = nptGlobals.IlaInsertion_SpanLoss;
//   console.log("Ila type chnaged to ::", ilaType);

//   if (ilaType == nptGlobals.getDefaultLinkStr()) {
//     numOfIla = Math.floor(spanLoss / nptGlobals.getDefaultLinkMaxSpanLoss());
//   } else if (ilaType == nptGlobals.getRamanHybridLinkStr()) {
//     numOfIla = Math.floor(spanLoss / nptGlobals.getRamanHybridMaxSpanloss());
//   } else if (ilaType == nptGlobals.getRamanDraLinkStr()) {
//     numOfIla = Math.floor(spanLoss / nptGlobals.getRamanDraMaxSpanloss());
//   }

//   console.log(
//     "ilaType::",
//     ilaType,
//     " numOfIla :: ",
//     numOfIla,
//     " SpanLoss::",
//     spanLoss
//   );
//   let template = fGetManualIlaInsertionTemplate(ilaType, numOfIla);

//   $("#ilaLinksInputDiv")
//     .empty()
//     .append(template);
// });
