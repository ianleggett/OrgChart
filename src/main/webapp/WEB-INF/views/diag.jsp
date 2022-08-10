
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="header.jsp" />

<script src="https://unpkg.com/gojs/release/go-debug.js"></script>

<jsp:include page="menu.jsp" />

<script type="text/javascript">
	var csrfname = "${_csrf.parameterName}";
	var csrfvalue = "${_csrf.token}";

	function pageStart() {	
		console.log('viewname = ${view}');
		$.getJSON('orgdata_gojs.json?v=${view}', function(dataIn){
			//console.log(JSON.stringify(dataIn));
			test();
	      	myDiagram.model = new go.GraphLinksModel(dataIn.nodedata, dataIn.linkdata);
		}); 
	}

	function test(){
		   // Since 2.2 you can also author concise templates with method chaining instead of GraphObject.make
	      // For details, see https://gojs.net/latest/intro/buildingObjects.html
	      const $ = go.GraphObject.make;  // for conciseness in defining templates

	      myDiagram =
	        $(go.Diagram, "myDiagramDiv",  // create a Diagram for the DIV HTML element
	          {
	            // allow double-click in background to create a new node
	            "clickCreatingTool.archetypeNodeData": { text: "Node", color: "white" },

	            // allow Ctrl-G to call groupSelection()
	            "commandHandler.archetypeGroupData": { text: "Group", isGroup: true, color: "blue" },

	            // enable undo & redo
	            "undoManager.isEnabled": true
	          });

	      // Define the appearance and behavior for Nodes:

	      // First, define the shared context menu for all Nodes, Links, and Groups.

	      // To simplify this code we define a function for creating a context menu button:
	      function makeButton(text, action, visiblePredicate) {
	        return $("ContextMenuButton",
	          $(go.TextBlock, text),
	          { click: action },
	          // don't bother with binding GraphObject.visible if there's no predicate
	          visiblePredicate ? new go.Binding("visible", "", (o, e) => o.diagram ? visiblePredicate(o, e) : false).ofObject() : {});
	      }

	      // a context menu is an Adornment with a bunch of buttons in them
	      var partContextMenu =
	        $("ContextMenu",
	          makeButton("Properties",
	            (e, obj) => {  // OBJ is this Button
	              var contextmenu = obj.part;  // the Button is in the context menu Adornment
	              var part = contextmenu.adornedPart;  // the adornedPart is the Part that the context menu adorns
	              // now can do something with PART, or with its data, or with the Adornment (the context menu)
	              if (part instanceof go.Link) alert(linkInfo(part.data));
	              else if (part instanceof go.Group) alert(groupInfo(contextmenu));
	              else alert(nodeInfo(part.data));
	            }),
	          makeButton("Cut",
	            (e, obj) => e.diagram.commandHandler.cutSelection(),
	            o => o.diagram.commandHandler.canCutSelection()),
	          makeButton("Copy",
	            (e, obj) => e.diagram.commandHandler.copySelection(),
	            o => o.diagram.commandHandler.canCopySelection()),
	          makeButton("Paste",
	            (e, obj) => e.diagram.commandHandler.pasteSelection(e.diagram.toolManager.contextMenuTool.mouseDownPoint),
	            o => o.diagram.commandHandler.canPasteSelection(o.diagram.toolManager.contextMenuTool.mouseDownPoint)),
	          makeButton("Delete",
	            (e, obj) => e.diagram.commandHandler.deleteSelection(),
	            o => o.diagram.commandHandler.canDeleteSelection()),
	          makeButton("Undo",
	            (e, obj) => e.diagram.commandHandler.undo(),
	            o => o.diagram.commandHandler.canUndo()),
	          makeButton("Redo",
	            (e, obj) => e.diagram.commandHandler.redo(),
	            o => o.diagram.commandHandler.canRedo()),
	          makeButton("Group",
	            (e, obj) => e.diagram.commandHandler.groupSelection(),
	            o => o.diagram.commandHandler.canGroupSelection()),
	          makeButton("Ungroup",
	            (e, obj) => e.diagram.commandHandler.ungroupSelection(),
	            o => o.diagram.commandHandler.canUngroupSelection())
	        );

	      function nodeInfo(d) {  // Tooltip info for a node data object
	        var str = "Node " + d.key + ": " + d.text + "\n";
	        if (d.group)
	          str += "member of " + d.group;
	        else
	          str += "top-level node";
	        return str;
	      }

	      function textStyle() {
	    	    return { font: "9pt  Segoe UI,sans-serif", stroke: "black" };
	    	  }
	      
	      // These nodes have text surrounded by a rounded rectangle
	      // whose fill color is bound to the node data.
	      // The user can drag a node by dragging its TextBlock label.
	      // Dragging from the Shape will start drawing a new link.
	myDiagram.nodeTemplate =
			  $(go.Node, "Auto",
			    { locationSpot: go.Spot.Center },
			    new go.Binding("location", "loc", go.Point.parse),
			    $(go.Shape, "RoundedRectangle",
			            {
			              fill: "white", // the default fill, if there is no data bound value
			              portId: "", cursor: "pointer",  // the Shape is the port, not the whole Node
			              // allow all kinds of links from and to this port
			              fromLinkable: true, fromLinkableSelfNode: true, fromLinkableDuplicates: true,
			              toLinkable: true, toLinkableSelfNode: true, toLinkableDuplicates: true
			            },
			            new go.Binding("fill", "color")),
			            $(go.Panel, "Table",
		    		            {		    		              
		    		              margin: new go.Margin(6, 10, 0, 6),
		    		              defaultAlignment: go.Spot.Left
		    		            },
		    		            $(go.RowColumnDefinition, { column: 2, width: 4 }),
		    		            $(go.TextBlock, textStyle(),  // the name
		    		              {		    		                
		    		                row: 0, column: 0, columnSpan: 5,
		    		                font: "bold 10pt Segoe UI,sans-serif",		    		                
		    		                editable: true, isMultiline: false,
		    		                minSize: new go.Size(50, 16)
		    		              },
		    		              new go.Binding("text", "text").makeTwoWay()),
		    		              $(go.TextBlock, textStyle(),
		    	    		              {
		    	    		                row: 1, column: 0, columnSpan: 5,
		    	    		                editable: true, isMultiline: false,
		    	    		                minSize: new go.Size(50, 14),
		    	    		                margin: new go.Margin(0, 0, 0, 3)
		    	    		              },
		    	    		      new go.Binding("text", "title").makeTwoWay()),
			            )
			            
			            
			           
			    	            
			    	    
			  );

	    	  
	    	  
	    	  /******************************************************/
	    	  
	    	  
	    	  
	      function linkInfo(d) {  // Tooltip info for a link data object
	        return "Link:\nfrom " + d.from + " to " + d.to;
	      }

	      // The link shape and arrowhead have their stroke brush data bound to the "color" property
	      myDiagram.linkTemplate =
	        $(go.Link,
	          { toShortLength: 3, relinkableFrom: true, relinkableTo: true },  // allow the user to relink existing links
	          { routing: go.Link.AvoidsNodes,
	        	 curve: go.Link.JumpOver, 
	        	  corner: 10 },
	          $(go.Shape,
	            { strokeWidth: 2 },
	            new go.Binding("stroke", "color")),
	          $(go.Shape,
	            { toArrow: "Standard", stroke: null },
	            new go.Binding("fill", "color")),
	          { // this tooltip Adornment is shared by all links
	            toolTip:
	              $("ToolTip",
	                $(go.TextBlock, { margin: 4 },  // the tooltip shows the result of calling linkInfo(data)
	                  new go.Binding("text", "", linkInfo))
	              ),
	            // the same context menu Adornment is shared by all links
	            contextMenu: partContextMenu
	          }
	        );

	      // Define the appearance and behavior for Groups:

	      function groupInfo(adornment) {  // takes the tooltip or context menu, not a group node data object
	        var g = adornment.adornedPart;  // get the Group that the tooltip adorns
	        var mems = g.memberParts.count;
	        var links = 0;
	        g.memberParts.each(part => {
	          if (part instanceof go.Link) links++;
	        });
	        return "Group " + g.data.key + ": " + g.data.text + "\n" + mems + " members including " + links + " links";
	      }

	      // Groups consist of a title in the color given by the group node data
	      // above a translucent gray rectangle surrounding the member parts
	      myDiagram.groupTemplate =
	        $(go.Group, "Vertical",
	          {
	            selectionObjectName: "PANEL",  // selection handle goes around shape, not label
	            ungroupable: true  // enable Ctrl-Shift-G to ungroup a selected Group
	          },
	          $(go.TextBlock,
	            {
	              //alignment: go.Spot.Right,
	              font: "bold 19px sans-serif",
	              isMultiline: false,  // don't allow newlines in text
	              editable: true  // allow in-place editing by user
	            },
	            new go.Binding("text", "text").makeTwoWay(),
	            new go.Binding("stroke", "color")),
	          $(go.Panel, "Auto",
	            { name: "PANEL" },
	            $(go.Shape, "Rectangle",  // the rectangular shape around the members
	              {
	                fill: "rgba(128,128,128,0.2)", stroke: "gray", strokeWidth: 3,
	                portId: "", cursor: "pointer",  // the Shape is the port, not the whole Node
	                // allow all kinds of links from and to this port
	                fromLinkable: true, fromLinkableSelfNode: true, fromLinkableDuplicates: true,
	                toLinkable: true, toLinkableSelfNode: true, toLinkableDuplicates: true
	              }),
	            $(go.Placeholder, { margin: 10, background: "transparent" })  // represents where the members are
	          ),
	          { // this tooltip Adornment is shared by all groups
	            toolTip:
	              $("ToolTip",
	                $(go.TextBlock, { margin: 4 },
	                  // bind to tooltip, not to Group.data, to allow access to Group properties
	                  new go.Binding("text", "", groupInfo).ofObject())
	              ),
	            // the same context menu Adornment is shared by all groups
	            contextMenu: partContextMenu
	          }
	        );

	      // Define the behavior for the Diagram background:

	      function diagramInfo(model) {  // Tooltip info for the diagram's model
	        return "Model:\n" + model.nodeDataArray.length + " nodes, " + model.linkDataArray.length + " links";
	      }

	      // provide a tooltip for the background of the Diagram, when not over any Part
	      myDiagram.toolTip =
	        $("ToolTip",
	          $(go.TextBlock, { margin: 4 },
	            new go.Binding("text", "", diagramInfo))
	        );

	      // provide a context menu for the background of the Diagram, when not over any Part
	      myDiagram.contextMenu =
	        $("ContextMenu",
	          makeButton("Paste",
	            (e, obj) => e.diagram.commandHandler.pasteSelection(e.diagram.toolManager.contextMenuTool.mouseDownPoint),
	            o => o.diagram.commandHandler.canPasteSelection(o.diagram.toolManager.contextMenuTool.mouseDownPoint)),
	          makeButton("Undo",
	            (e, obj) => e.diagram.commandHandler.undo(),
	            o => o.diagram.commandHandler.canUndo()),
	          makeButton("Redo",
	            (e, obj) => e.diagram.commandHandler.redo(),
	            o => o.diagram.commandHandler.canRedo())
	        );

	      myDiagram.zoomToRect(myDiagram.documentBounds);
	}
		
</script>

   <div id="myDiagramDiv"
     style="width:90vw; margin: auto; height: 80vh; background-color: #DAE4E4;"></div>

</body>
</html>
