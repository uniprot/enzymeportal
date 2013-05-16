function $x(pNd){
	var lThis;
	switch(typeof (pNd)){
		case 'string':lThis = document.getElementById(pNd);break;
		case 'object':lThis = pNd;break;
		default:return false;break;
	}
	return (lThis.nodeType == 1)?lThis:false;
}

function $u_Narray(pNd){return (pNd.length == 1)?pNd[0]:pNd;}
function $u_Carray(pNd){return ($x(pNd))?[pNd]:pNd;}

function $x_Class(pNd,pClass){
	if($x(pNd)){pNd = [pNd];}
	var l=pNd.length;
	for(var i=0;i<l;i++){if($x(pNd[i])){$x(pNd[i]).className=pClass;}}
	return $u_Narray(pNd);
}

function $x_Style(pNd,pStyle,pString){
	pNd = $u_Carray(pNd);
	for(var i=0;i<pNd.length;i++){
		var node = $x(pNd[i]);
		(!!node)?node.style[pStyle]=pString:null;
	}
	return $u_Narray(pNd);
}

function $x_Hide(pNd){return $x_Style(pNd,'display','none');}
function $x_Show(pNd){return $x_Style(pNd,'display','');}

function $x_HideAllExcept(pNd,pNdArray){
	var l_Node = $x(pNd);
	if(l_Node){
		$x_Hide(pNdArray);
		$x_Show(l_Node);
	}
	return l_Node;
}

function $x_HideSiblings(pNd){
	return $x_HideAllExcept(pNd,$x(pNd).parentNode.childNodes);
}

function $x_ByClass(pClass,pNd,pTag){
	var classElements=[];
	if (!pNd){pNd = document;}else{pNd = $x(pNd);}
	if (!pTag){pTag = '*';}
	var els = pNd.getElementsByTagName(pTag);
	var elsLen = els.length;
	var pattern = new RegExp("(^|\\s)"+pClass+"(\\s|$)");
	for (var i=0,j=0;i<elsLen;i++){
		if (pattern.test(els[i].className)){
			classElements[j] = els[i];
			j++;
		}
	}
	return classElements;
}

function $x_SetSiblingsClass(pNd,pClass,pNdClass){
	var l_Node = $x(pNd);
	if(l_Node){
		var l_NodeSibs = l_Node.parentNode.childNodes;
		$x_Class(l_NodeSibs,pClass);
		if(pNdClass){$x_Class(l_Node,pNdClass);}
	}
	return l_Node;
}



function $d_TabClick(pTab,pTabPanel,pClass,pTabsArray,pTabsPanelArray){
	var lTabPanel = $x(pTabPanel),lclassName=(pClass)?pClass:'current';
	if(!pTabsPanelArray){$x_HideSiblings(lTabPanel);}else{$x_HideAllExcept(pTabPanel,pTabsArray);}
	if(!pTabsArray){$x_SetSiblingsClass(pTab,'',lclassName);}
	else{$x_Class(pTabsArray,'');$x(pTab).className=lclassName;}
}

/**
 * @ignore
 * */
function getCookieVal (offset){
   var endstr = document.cookie.indexOf (";", offset);
   if (endstr==-1){endstr = document.cookie.length;}
   return unescape(document.cookie.substring(offset, endstr));
}

/**
 * Returns the value of cookie name (pName).
 * @function
 * @param {String} pName
 * */
function GetCookie(pName){
	var arg = pName + "=";
	var alen = arg.length;
	var clen = document.cookie.length;
	var i = 0;
	while (i < clen){
		var j = i + alen;
		if(document.cookie.substring(i,j)==arg){return getCookieVal(j);}
		i = document.cookie.indexOf(" ", i) + 1;
		if(i===0){break;} 
	}
	return null;
}

/**
 * Sets a cookie (pName) to a specified value (pValue).
 * @function
 * @param {String} pName
 * @param {String} pValue
 * */
function SetCookie (pName,pValue) {
   var argv = arguments;
   var argc = arguments.length;
   var expires = (argc > 2) ? argv[2] : null;
   var path = (argc > 3) ? argv[3] : null;
   var domain = (argc > 4) ? argv[4] : null;
   var secure = (argc > 5) ? true : false;
   document.cookie = pName + "=" + escape (pValue) +
        ((expires === null) ? "" : ("; expires=" + expires.toGMTString())) +
        ((path === null) ? "" : ("; path=" + path)) +
        ((domain === null) ? "" : ("; domain=" + domain)) +
        ((secure === true) ? "; secure" : "");
}



function $h(pThis,pThat){
	$x('current').id = '';
	pThis.parentNode.parentNode.id = 'current';
	$d_TabClick(pThis,pThat);
	SetCookie ('dbdoc_tab',pThis.id+':'+pThat);
}

function $init(pThis,pThat){
	var lCook = GetCookie ('dbdoc_tab',pThis.id);
    if(!!lCook){
		var lArray = lCook.split(":");
		 if($x(lArray[0])){$h($x(lArray[0]),lArray[1]);}
		 else{$h($x(pThis),pThat);}
	}else{
		$h($x(pThis),pThat);
	}
	$x_Hide('loading');
	$x_Show('tab-panes');
}

function html_GetTarget(e){
	var targ,lEvt;
	if(!e){e = window.event;}
	if(e.target){targ = e.target;}
	else if(e.srcElement){targ = e.srcElement;}
	if(targ.nodeType == 3){targ = targ.parentNode;}// defeat Safari bug
	return targ;  
}

function $d(pThis){
	$x_SetSiblingsClass($x(pThis),'','current');
	parent.ObjectDetailsFrame.location = pThis +'_details.html';
}

function $n(pThis){
	$x_SetSiblingsClass($x(pThis),'','current');
	parent.ObjectDetailsFrame.location = 'about:blank';
	parent.DBObjectsFrame.gType = pThis;
	parent.DBObjectsFrame.location = pThis +'/index.html';
}

var gRegex=false;
function $d_Find(pThis,pString,pTags,pClass){
       if(!pTags){pTags = 'DIV';}
       pThis = $x(pThis);
       if(pThis){
           var d=pThis.getElementsByTagName(pTags);
           pThis.style.display="none";
           if(!gRegex){gRegex =new RegExp("test");}
           gRegex.compile(pString,"i");
           for(var i=0,len=d.length ;i<len;i++){
               if(gRegex.test(d[i].innerHTML)){
                   d[i].style.display="block";
               }
               else{d[i].style.display="none";}
           }
       pThis.style.display="block";
   }
   return;
}


//========================
// Unobtrusive JavaScript
//========================


//======
// TREE
//======

function getEventTarget(anEvent){
  var theTarget;
  if (!anEvent) {
    anEvent = window.event;
  }
  if (anEvent.target) {
    theTarget = anEvent.target; // MS IE different (of course)
  } else if (anEvent.srcElement) {
    theTarget = anEvent.srcElement;
  }
  if (theTarget.nodeType == 3) {
    theTarget = targ.parentNode; // defeat Safari bug
  }
  return theTarget;
}

function hasClassName(element, className) {
  var elementClassName = element.className;
  return (elementClassName.length > 0 &&
          (elementClassName == className ||
           new RegExp("(^|\\s)" + className + "(\\s|$)").test(elementClassName)
          ));
}

function addClassName(element, className) {
  if (!hasClassName(element, className)) {
      element.className += (element.className ? ' ' : '') + className;
  }
  return element;
}

function removeClassName(element, className) {
  if (hasClassName(element, className)) {
    // Replace className and surrounding whitespace to single space
    // Then remove leading and trailing whitespace
    element.className = element.className.replace(
        new RegExp("(^|\\s+)" + className + "(\\s+|$)"), ' ').replace(/^\s+/, '').replace(/\s+$/, '')
  }
  return element;
}

function toggleBranch(anEvent) {
  var branch, subBranches;
  branch = getEventTarget(anEvent);
  // If user clicks in area to left of tree (background UL area),
  // we don't want to do anything
  // FIXME: BUT what about clicking to left of sub list & target 
  // being parent LI
   if ("UL" == branch.tagName ||
       "OL" == branch.tagName) {
         return;
   }
  // We expect branch to be a li, but Chrome treats click on li icon as
  // clicking on it's content (even though icon is "outside") so need to
  // do some tweaking here
  while ("LI" != branch.tagName && branch.parentNode) {
  	branch = branch.parentNode;
  }
  if ("LI" == branch.tagName) {
    subBranches = branch.getElementsByTagName("ul");
    if (subBranches.length > 0) {
      var subBranch = subBranches[0];
      if (subBranch.style.display == "block") {
        subBranch.style.display = "none";
        removeClassName(branch, "expanded");
        addClassName(branch, "collapsed");
      } else {
        subBranch.style.display = "block";
        removeClassName(branch, "collapsed");
        addClassName(branch, "expanded");
      }
    }
  }
}

function addEventListener(anElement, anEventName, anEventHandler) {
  if (anElement.addEventListener) {
    anElement.addEventListener(anEventName, anEventHandler, false);
  } else if (tree.attachEvent) {
    ieEventName = "on" + anEventName; // MS IE different (of course)
    anElement.attachEvent(ieEventName, anEventHandler);
  }
}

function activateTree(tree) {
  // Collapse (hide) the tree branches
  var branches = tree.getElementsByTagName("ul");
  for (var i=0; i < branches.length; i++) {
    branches[i].style.display="none";
  }
  // Style the items (see dbdoc.css)
  var items = tree.getElementsByTagName("li");
  for (var i=0; i < items.length; i++) {
  	if (items[i].getElementsByTagName("li").length > 0) {
  		addClassName(items[i], "collapsed");
  	} else {
  		addClassName(items[i], "noChildren");
  	}
  }
  addEventListener(tree, "click", toggleBranch);
}

function initializeTrees() {
  var trees = document.getElementsByName("Tree");
  for (var idx = 0; idx < trees.length; idx++) {
    activateTree(trees[idx]);
  }
}

//===============
// Frame Loaders
//===============

//http://stackoverflow.com/questions/984510/what-is-my-script-src-url/984656#984656
var scriptSource = (function(scripts) {
  var scripts = document.getElementsByTagName('script'),
       script = scripts[scripts.length - 1];

  return (script.getAttribute.length !== undefined) ?
    //FF/Chrome/Safari
    script.src : //(only FYI, this would work also in IE8)
    //IE 6/7/8
    script.getAttribute('src', 4); //using 4 (and not -1) see MSDN http://msdn.microsoft.com/en-us/library/ms536429(VS.85).aspx
}());
// Now that we know where we are, compose URL fo empty.html (which should
// be in the same directory as this script)
var emptyHtml = scriptSource.substr(0, scriptSource.length - 8) + "empty.html";

function loadDBObjects(src) {
    parent.DBObjectsFrame.location.href = src;
}

function loadDetails(src) {
    parent.ObjectDetailsFrame.location.href = src;
}

function loadChildren(src) {
    parent.ObjectDetailsChildrenFrame.location.href = src;
}

function loadDependentFrames() {
  var found = false;
  // ObjectsFrame
  // ObjectTypeFrame
  // DBObjectsFrame
  if (window.name == "ObjectsFrame") {
    for (idx = 0; idx < document.links.length; idx++) {
      if (document.links[idx].target == "ObjectDetailsFrame") {
        loadDetails(document.links[idx].href);
        found = true;
        break;
      }
    }
    if (!found) {
      loadDetails(emptyHtml);
    }
  } else if (window.name == "ObjectTypeFrame") {
    for (idx = 0; idx < document.links.length; idx++) {
      if (document.links[idx].target == "DBObjectsFrame") {
        loadDBObjects(document.links[idx].href);
        found = true;
        break;
      }
    }
    if (!found) {
      loadChildren(emptyHtml);
    }
  } else if (window.name == "DBObjectsFrame") {
    for (idx = 0; idx < document.links.length; idx++) {
      if (document.links[idx].target == "ObjectDetailsFrame") {
        loadDetails(document.links[idx].href);
        found = true;
        break;
      }
    }
    if (!found) {
      loadChildren(emptyHtml);
    }
  } else if (window.name == "ObjectDetailsFrame") {
    for (idx = 0; idx < document.links.length; idx++) {
      if (document.links[idx].target == "ObjectDetailsChildrenFrame") {
        loadChildren(document.links[idx].href);
        found = true;
        break;
      }
    }
    if (!found) {
      loadChildren(emptyHtml);
    }
  }
}

// Initialization
function addLoadEventListener(func) {
  var prevOnLoad = window.onload;
  if (typeof window.onload != 'function') {
    // That is, not defined yet
    window.onload = func;
  } else {
    window.onload = function() {
      if (prevOnLoad) {
        prevOnLoad();
      }
      func();
    }
  }
}
addLoadEventListener(initializeTrees);
addLoadEventListener(loadDependentFrames);
