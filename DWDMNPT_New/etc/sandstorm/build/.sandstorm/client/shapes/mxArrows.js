/**
 * $Id: mxArrows.js,v 1.1.1.1 2016/10/07 10:05:31 hiten Exp $
 * Copyright (c) 2006-2016, JGraph Ltd
 */

//**********************************************************************************************************************************************************
//Arrow
//**********************************************************************************************************************************************************
/**
* Extends mxShape.
*/
function mxShapeArrows2Arrow(bounds, fill, stroke, strokewidth)
{
	mxShape.call(this);
	this.bounds = bounds;
	this.fill = fill;
	this.stroke = stroke;
	this.strokewidth = (strokewidth != null) ? strokewidth : 1;
	this.dy = 0.5;
	this.dx = 0.5;
	this.notch = 0;
};

/**
* Extends mxShape.
*/
mxUtils.extend(mxShapeArrows2Arrow, mxActor);

mxShapeArrows2Arrow.prototype.cst = {
		ARROW : 'mxgraph.arrows2.arrow'
};

/**
* Function: paintVertexShape
* 
* Paints the vertex shape.
*/
mxShapeArrows2Arrow.prototype.paintVertexShape = function(c, x, y, w, h)
{
	c.translate(x, y);

	var dy = h * 0.5 * Math.max(0, Math.min(1, parseFloat(mxUtils.getValue(this.style, 'dy', this.dy))));
	var dx = Math.max(0, Math.min(w, parseFloat(mxUtils.getValue(this.style, 'dx', this.dx))));
	var notch = Math.max(0, Math.min(w, parseFloat(mxUtils.getValue(this.style, 'notch', this.notch))));

	c.begin();
	c.moveTo(0, dy);
	c.lineTo(w - dx, dy);
	c.lineTo(w - dx, 0);
	c.lineTo(w, h * 0.5);
	c.lineTo(w - dx, h);
	c.lineTo(w - dx, h - dy);
	c.lineTo(0, h - dy);
	c.lineTo(notch, h * 0.5);
	c.close();
	c.fillAndStroke();
};

mxCellRenderer.registerShape(mxShapeArrows2Arrow.prototype.cst.ARROW, mxShapeArrows2Arrow);

mxShapeArrows2Arrow.prototype.constraints = null;

Graph.handleFactory[mxShapeArrows2Arrow.prototype.cst.ARROW] = function(state)
{
	var handles = [Graph.createHandle(state, ['dx', 'dy'], function(bounds)
			{
				var dx = Math.max(0, Math.min(bounds.width, parseFloat(mxUtils.getValue(this.state.style, 'dx', this.dx))));
				var dy = Math.max(0, Math.min(1, parseFloat(mxUtils.getValue(this.state.style, 'dy', this.dy))));

				return new mxPoint(bounds.x + bounds.width - dx, bounds.y + dy * bounds.height / 2);
			}, function(bounds, pt)
			{
				this.state.style['dx'] = Math.round(100 * Math.max(0, Math.min(bounds.width - parseFloat(mxUtils.getValue(this.state.style, 'notch', this.notch)), bounds.x + bounds.width - pt.x))) / 100;
				this.state.style['dy'] = Math.round(100 * Math.max(0, Math.min(1, (((pt.y - bounds.y) / bounds.height) * 2)))) / 100;
			})];
			
	var handle2 = Graph.createHandle(state, ['notch'], function(bounds)
			{
				var notch = Math.max(0, Math.min(bounds.width, parseFloat(mxUtils.getValue(this.state.style, 'notch', this.notch))));

				return new mxPoint(bounds.x + notch, bounds.y + bounds.height / 2);
			}, function(bounds, pt)
			{
				this.state.style['notch'] = Math.round(100 * Math.max(0, Math.min(bounds.width - parseFloat(mxUtils.getValue(this.state.style, 'dx', this.dx)), pt.x - bounds.x))) / 100;
			});
	
	handles.push(handle2);
	
	return handles;

}

//**********************************************************************************************************************************************************
//Two Way Arrow
//**********************************************************************************************************************************************************
/**
* Extends mxShape.
*/
function mxShapeArrows2TwoWayArrow(bounds, fill, stroke, strokewidth)
{
	mxShape.call(this);
	this.bounds = bounds;
	this.fill = fill;
	this.stroke = stroke;
	this.strokewidth = (strokewidth != null) ? strokewidth : 1;
	this.dy = 0.5;
	this.dx = 0.5;
};

/**
* Extends mxShape.
*/
mxUtils.extend(mxShapeArrows2TwoWayArrow, mxActor);

mxShapeArrows2TwoWayArrow.prototype.cst = {
		TWO_WAY_ARROW : 'mxgraph.arrows2.twoWayArrow'
};

/**
* Function: paintVertexShape
* 
* Paints the vertex shape.
*/
mxShapeArrows2TwoWayArrow.prototype.paintVertexShape = function(c, x, y, w, h)
{
	c.translate(x, y);

	var dy = h * 0.5 * Math.max(0, Math.min(1, parseFloat(mxUtils.getValue(this.style, 'dy', this.dy))));
	var dx = Math.max(0, Math.min(w, parseFloat(mxUtils.getValue(this.style, 'dx', this.dx))));

	c.begin();
	c.moveTo(dx, dy);
	c.lineTo(w - dx, dy);
	c.lineTo(w - dx, 0);
	c.lineTo(w, h * 0.5);
	c.lineTo(w - dx, h);
	c.lineTo(w - dx, h - dy);
	c.lineTo(dx, h - dy);
	c.lineTo(dx, h);
	c.lineTo(0, h * 0.5);
	c.lineTo(dx, 0);
	c.close();
	c.fillAndStroke();
};

mxCellRenderer.registerShape(mxShapeArrows2TwoWayArrow.prototype.cst.TWO_WAY_ARROW, mxShapeArrows2TwoWayArrow);

mxShapeArrows2TwoWayArrow.prototype.constraints = null;

Graph.handleFactory[mxShapeArrows2TwoWayArrow.prototype.cst.TWO_WAY_ARROW] = function(state)
{
	var handles = [Graph.createHandle(state, ['dx', 'dy'], function(bounds)
			{
				var dx = Math.max(0, Math.min(bounds.width / 2, parseFloat(mxUtils.getValue(this.state.style, 'dx', this.dx))));
				var dy = Math.max(0, Math.min(1, parseFloat(mxUtils.getValue(this.state.style, 'dy', this.dy))));

				return new mxPoint(bounds.x + bounds.width - dx, bounds.y + dy * bounds.height / 2);
			}, function(bounds, pt)
			{
				this.state.style['dx'] = Math.round(100 * Math.max(0, Math.min(bounds.width / 2, bounds.x + bounds.width - pt.x))) / 100;
				this.state.style['dy'] = Math.round(100 * Math.max(0, Math.min(1, (((pt.y - bounds.y) / bounds.height) * 2)))) / 100;
			})];
	
	return handles;

}

//**********************************************************************************************************************************************************
//Stylised Arrow
//**********************************************************************************************************************************************************
/**
* Extends mxShape.
*/
function mxShapeArrows2StylisedArrow(bounds, fill, stroke, strokewidth)
{
	mxShape.call(this);
	this.bounds = bounds;
	this.fill = fill;
	this.stroke = stroke;
	this.strokewidth = (strokewidth != null) ? strokewidth : 1;
	this.dy = 0.5;
	this.dx = 0.5;
	this.notch = 0;
	this.feather = 0.5;
};

/**
* Extends mxShape.
*/
mxUtils.extend(mxShapeArrows2StylisedArrow, mxActor);

mxShapeArrows2StylisedArrow.prototype.cst = {
		STYLISED_ARROW : 'mxgraph.arrows2.stylisedArrow'
};

/**
* Function: paintVertexShape
* 
* Paints the vertex shape.
*/
mxShapeArrows2StylisedArrow.prototype.paintVertexShape = function(c, x, y, w, h)
{
	c.translate(x, y);

	var dy = h * 0.5 * Math.max(0, Math.min(1, parseFloat(mxUtils.getValue(this.style, 'dy', this.dy))));
	var dx = Math.max(0, Math.min(w, parseFloat(mxUtils.getValue(this.style, 'dx', this.dx))));
	var notch = Math.max(0, Math.min(w, parseFloat(mxUtils.getValue(this.style, 'notch', this.notch))));
	var feather = h * 0.5 * Math.max(0, Math.min(1, parseFloat(mxUtils.getValue(this.style, 'feather', this.feather))));

	c.begin();
	c.moveTo(0, feather);
	c.lineTo(w - dx, dy);
	c.lineTo(w - dx - 10, 0);
	c.lineTo(w, h * 0.5);
	c.lineTo(w - dx - 10, h);
	c.lineTo(w - dx, h - dy);
	c.lineTo(0, h - feather);
	c.lineTo(notch, h * 0.5);
	c.close();
	c.fillAndStroke();
};

mxCellRenderer.registerShape(mxShapeArrows2StylisedArrow.prototype.cst.STYLISED_ARROW, mxShapeArrows2StylisedArrow);

mxShapeArrows2StylisedArrow.prototype.constraints = null;

Graph.handleFactory[mxShapeArrows2StylisedArrow.prototype.cst.STYLISED_ARROW] = function(state)
{
	var handles = [Graph.createHandle(state, ['dx', 'dy'], function(bounds)
			{
				var dx = Math.max(0, Math.min(bounds.width - 10, parseFloat(mxUtils.getValue(this.state.style, 'dx', this.dx))));
				var dy = Math.max(0, Math.min(1, parseFloat(mxUtils.getValue(this.state.style, 'dy', this.dy))));

				return new mxPoint(bounds.x + bounds.width - dx, bounds.y + dy * bounds.height / 2);
			}, function(bounds, pt)
			{
				this.state.style['dx'] = Math.round(100 * Math.max(0, Math.min(bounds.width - 10, bounds.width - parseFloat(mxUtils.getValue(this.state.style, 'notch', this.notch)), bounds.x + bounds.width - pt.x))) / 100;
				this.state.style['dy'] = Math.round(100 * Math.max(0, Math.min(1, (((pt.y - bounds.y) / bounds.height) * 2)))) / 100;
			})];
			
	var handle2 = Graph.createHandle(state, ['notch'], function(bounds)
			{
				var notch = Math.max(0, Math.min(bounds.width - parseFloat(mxUtils.getValue(this.state.style, 'dx', this.dx)), parseFloat(mxUtils.getValue(this.state.style, 'notch', this.notch))));

				return new mxPoint(bounds.x + notch, bounds.y + bounds.height / 2);
			}, function(bounds, pt)
			{
				this.state.style['notch'] = Math.round(100 * Math.max(0, Math.min(bounds.width - parseFloat(mxUtils.getValue(this.state.style, 'dx', this.dx)), pt.x - bounds.x))) / 100;
			});
	
	handles.push(handle2);
	
	var handle3 = Graph.createHandle(state, ['feather'], function(bounds)
			{
				var feather = Math.max(0, Math.min(1, parseFloat(mxUtils.getValue(this.state.style, 'feather', this.dy))));

				return new mxPoint(bounds.x, bounds.y + feather * bounds.height / 2);
			}, function(bounds, pt)
			{
				this.state.style['feather'] = Math.round(100 * Math.max(0, Math.min(1, (((pt.y - bounds.y) / bounds.height) * 2)))) / 100;
			});
	
	handles.push(handle3);
	
	return handles;

}

//**********************************************************************************************************************************************************
//Sharp Arrow
//**********************************************************************************************************************************************************
/**
* Extends mxShape.
*/
function mxShapeArrows2SharpArrow(bounds, fill, stroke, strokewidth)
{
	mxShape.call(this);
	this.bounds = bounds;
	this.fill = fill;
	this.stroke = stroke;
	this.strokewidth = (strokewidth != null) ? strokewidth : 1;
	this.dy1 = 0.5;
	this.dx1 = 0.5;
	this.dx2 = 0.5;
	this.notch = 0;
};

/**
* Extends mxShape.
*/
mxUtils.extend(mxShapeArrows2SharpArrow, mxActor);

mxShapeArrows2SharpArrow.prototype.cst = {
		SHARP_ARROW : 'mxgraph.arrows2.sharpArrow'
};

/**
* Function: paintVertexShape
* 
* Paints the vertex shape.
*/
mxShapeArrows2SharpArrow.prototype.paintVertexShape = function(c, x, y, w, h)
{
	c.translate(x, y);

	var dy1 = h * 0.5 * Math.max(0, Math.min(1, parseFloat(mxUtils.getValue(this.style, 'dy1', this.dy1))));
	var dx1 = Math.max(0, Math.min(w, parseFloat(mxUtils.getValue(this.style, 'dx1', this.dx1))));
	var dx2 = Math.max(0, Math.min(w, parseFloat(mxUtils.getValue(this.style, 'dx2', this.dx2))));
	var notch = Math.max(0, Math.min(w, parseFloat(mxUtils.getValue(this.style, 'notch', this.notch))));

	var dx1a = Math.max(0, Math.min(w, parseFloat(mxUtils.getValue(this.style, 'dx1', this.dx1))));
	var dy1a = h * 0.5 * Math.max(0, Math.min(h, parseFloat(mxUtils.getValue(this.style, 'dy1', this.dy1))));

	var x2 = 0;
	
	if (h != 0)
	{
		x2 = dx1a + dx2 * dy1a * 2 / h;
	}
	
	c.begin();
	c.moveTo(0, dy1);
	c.lineTo(w - dx1, dy1);
	c.lineTo(w - x2, 0);
	c.lineTo(w - dx2, 0);
	c.lineTo(w, h * 0.5);
	c.lineTo(w - dx2, h);
	c.lineTo(w - x2, h);
	c.lineTo(w - dx1, h - dy1);
	c.lineTo(0, h - dy1);
	c.lineTo(notch, h * 0.5);
	c.close();
	c.fillAndStroke();
};

mxCellRenderer.registerShape(mxShapeArrows2SharpArrow.prototype.cst.SHARP_ARROW, mxShapeArrows2SharpArrow);

mxShapeArrows2SharpArrow.prototype.constraints = null;

Graph.handleFactory[mxShapeArrows2SharpArrow.prototype.cst.SHARP_ARROW] = function(state)
{
	var handles = [Graph.createHandle(state, ['dx1', 'dy1'], function(bounds)
			{
				var dx1 = Math.max(0, Math.min(bounds.width, parseFloat(mxUtils.getValue(this.state.style, 'dx1', this.dx1))));
				var dy1 = Math.max(0, Math.min(1, parseFloat(mxUtils.getValue(this.state.style, 'dy1', this.dy1))));

				return new mxPoint(bounds.x + bounds.width - dx1, bounds.y + dy1 * bounds.height / 2);
			}, function(bounds, pt)
			{
				this.state.style['dx1'] = Math.round(100 * Math.max(0, Math.min(bounds.width - parseFloat(mxUtils.getValue(this.state.style, 'notch', this.notch)), bounds.x + bounds.width - pt.x))) / 100;
				this.state.style['dy1'] = Math.round(100 * Math.max(0, Math.min(1, (((pt.y - bounds.y) / bounds.height) * 2)))) / 100;
			})];
			
	var handle2 = Graph.createHandle(state, ['notch'], function(bounds)
			{
				var notch = Math.max(0, Math.min(bounds.width, parseFloat(mxUtils.getValue(this.state.style, 'notch', this.notch))));

				return new mxPoint(bounds.x + notch, bounds.y + bounds.height / 2);
			}, function(bounds, pt)
			{
				this.state.style['notch'] = Math.round(100 * Math.max(0, Math.min(bounds.width - parseFloat(mxUtils.getValue(this.state.style, 'dx1', this.dx1)), pt.x - bounds.x))) / 100;
			});
	
	handles.push(handle2);
	
	var handle3 = Graph.createHandle(state, ['dx2'], function(bounds)
			{
				var dx2 = Math.max(0, Math.min(bounds.width, parseFloat(mxUtils.getValue(this.state.style, 'dx2', this.dx2))));

				return new mxPoint(bounds.x + bounds.width - dx2, bounds.y);
			}, function(bounds, pt)
			{
				this.state.style['dx2'] = Math.round(100 * Math.max(0, Math.min(bounds.width - parseFloat(mxUtils.getValue(this.state.style, 'notch', this.notch)), bounds.x + bounds.width - pt.x))) / 100;
			});
	
	handles.push(handle3);
	
	return handles;

}

//**********************************************************************************************************************************************************
//Sharp Arrow2
//**********************************************************************************************************************************************************
/**
* Extends mxShape.
*/
function mxShapeArrows2SharpArrow2(bounds, fill, stroke, strokewidth)
{
	mxShape.call(this);
	this.bounds = bounds;
	this.fill = fill;
	this.stroke = stroke;
	this.strokewidth = (strokewidth != null) ? strokewidth : 1;
	this.dy1 = 0.5;
	this.dx1 = 0.5;
	this.dx2 = 0.5;
	this.dy3 = 0.5;
	this.dx3 = 0.5;

	this.notch = 0;
};

/**
* Extends mxShape.
*/
mxUtils.extend(mxShapeArrows2SharpArrow2, mxActor);

mxShapeArrows2SharpArrow2.prototype.cst = {
		SHARP_ARROW2 : 'mxgraph.arrows2.sharpArrow2'
};

/**
* Function: paintVertexShape
* 
* Paints the vertex shape.
*/
mxShapeArrows2SharpArrow2.prototype.paintVertexShape = function(c, x, y, w, h)
{
	c.translate(x, y);

	var dy1 = h * 0.5 * Math.max(0, Math.min(1, parseFloat(mxUtils.getValue(this.style, 'dy1', this.dy1))));
	var dx1 = Math.max(0, Math.min(w, parseFloat(mxUtils.getValue(this.style, 'dx1', this.dx1))));

	var dx2 = Math.max(0, Math.min(w, parseFloat(mxUtils.getValue(this.style, 'dx2', this.dx2))));
	
	var dy3 = h * 0.5 * Math.max(0, Math.min(1, parseFloat(mxUtils.getValue(this.style, 'dy3', this.dy3))));
	var dx3 = Math.max(0, Math.min(w, parseFloat(mxUtils.getValue(this.style, 'dx3', this.dx3))));
	
	var notch = Math.max(0, Math.min(w, parseFloat(mxUtils.getValue(this.style, 'notch', this.notch))));

	var dx1a = Math.max(0, Math.min(w, parseFloat(mxUtils.getValue(this.style, 'dx1', this.dx1))));
	var dy1a = h * 0.5 * Math.max(0, Math.min(h, parseFloat(mxUtils.getValue(this.style, 'dy1', this.dy1))));

	c.begin();
	c.moveTo(0, dy1);
	c.lineTo(w - dx1, dy1);
	c.lineTo(w - dx3, dy3);
	c.lineTo(w - dx2, 0);
	c.lineTo(w, h * 0.5);
	c.lineTo(w - dx2, h);
	c.lineTo(w - dx3, h - dy3);
	c.lineTo(w - dx1, h - dy1);
	c.lineTo(0, h - dy1);
	c.lineTo(notch, h * 0.5);
	c.close();
	c.fillAndStroke();
};

mxCellRenderer.registerShape(mxShapeArrows2SharpArrow2.prototype.cst.SHARP_ARROW2, mxShapeArrows2SharpArrow2);

mxShapeArrows2SharpArrow2.prototype.constraints = null;

Graph.handleFactory[mxShapeArrows2SharpArrow2.prototype.cst.SHARP_ARROW2] = function(state)
{
	var handles = [Graph.createHandle(state, ['dx1', 'dy1'], function(bounds)
			{
				var dx1 = Math.max(0, Math.min(bounds.width, parseFloat(mxUtils.getValue(this.state.style, 'dx1', this.dx1))));
				var dy1 = Math.max(0, Math.min(1, parseFloat(mxUtils.getValue(this.state.style, 'dy1', this.dy1))));

				return new mxPoint(bounds.x + bounds.width - dx1, bounds.y + dy1 * bounds.height / 2);
			}, function(bounds, pt)
			{
				this.state.style['dx1'] = Math.round(100 * Math.max(0, Math.min(bounds.width - parseFloat(mxUtils.getValue(this.state.style, 'notch', this.notch)), bounds.x + bounds.width - pt.x))) / 100;
				this.state.style['dy1'] = Math.round(100 * Math.max(0, Math.min(1, (((pt.y - bounds.y) / bounds.height) * 2)))) / 100;
			})];
			
	var handle2 = Graph.createHandle(state, ['notch'], function(bounds)
			{
				var notch = Math.max(0, Math.min(bounds.width, parseFloat(mxUtils.getValue(this.state.style, 'notch', this.notch))));

				return new mxPoint(bounds.x + notch, bounds.y + bounds.height / 2);
			}, function(bounds, pt)
			{
				this.state.style['notch'] = Math.round(100 * Math.max(0, Math.min(bounds.width - parseFloat(mxUtils.getValue(this.state.style, 'dx1', this.dx1)), pt.x - bounds.x))) / 100;
			});
	
	handles.push(handle2);
	
	var handle3 = Graph.createHandle(state, ['dx2'], function(bounds)
			{
				var dx2 = Math.max(0, Math.min(bounds.width, parseFloat(mxUtils.getValue(this.state.style, 'dx2', this.dx2))));

				return new mxPoint(bounds.x + bounds.width - dx2, bounds.y);
			}, function(bounds, pt)
			{
				this.state.style['dx2'] = Math.round(100 * Math.max(0, Math.min(bounds.width - parseFloat(mxUtils.getValue(this.state.style, 'notch', this.notch)), bounds.x + bounds.width - pt.x))) / 100;
			});
	
	handles.push(handle3);

	var handle4 = Graph.createHandle(state, ['dx3', 'dy3'], function(bounds)
			{
				var dx3 = Math.max(0, Math.min(bounds.width, parseFloat(mxUtils.getValue(this.state.style, 'dx3', this.dx3))));
				var dy3 = Math.max(0, Math.min(1 - parseFloat(mxUtils.getValue(this.state.style, 'dy1', this.dy1)) / 2, parseFloat(mxUtils.getValue(this.state.style, 'dy3', this.dy3))));

				return new mxPoint(bounds.x + bounds.width - dx3, bounds.y + dy3 * bounds.height / 2);
			}, function(bounds, pt)
			{
				this.state.style['dx3'] = Math.round(100 * Math.max(parseFloat(mxUtils.getValue(this.state.style, 'dx2', this.dx2)), Math.min(bounds.width, bounds.x + bounds.width - pt.x))) / 100;
				this.state.style['dy3'] = Math.round(100 * Math.max(0, Math.min(parseFloat(mxUtils.getValue(this.state.style, 'dy1', this.dy1)), (((pt.y - bounds.y) / bounds.height) * 2)))) / 100;
			});

	handles.push(handle4);

	return handles;
}

//**********************************************************************************************************************************************************
//Callout Arrow
//**********************************************************************************************************************************************************
/**
* Extends mxShape.
*/
function mxShapeArrows2CalloutArrow(bounds, fill, stroke, strokewidth)
{
	mxShape.call(this);
	this.bounds = bounds;
	this.fill = fill;
	this.stroke = stroke;
	this.strokewidth = (strokewidth != null) ? strokewidth : 1;
	this.dy = 0.5;
	this.dx = 0.5;
	this.notch = 0;
	this.arrowHead = 0;
};

/**
* Extends mxShape.
*/
mxUtils.extend(mxShapeArrows2CalloutArrow, mxActor);

mxShapeArrows2CalloutArrow.prototype.cst = {
		CALLOUT_ARROW : 'mxgraph.arrows2.calloutArrow'
};

/**
* Function: paintVertexShape
* 
* Paints the vertex shape.
*/
mxShapeArrows2CalloutArrow.prototype.paintVertexShape = function(c, x, y, w, h)
{
	c.translate(x, y);

	var dy = Math.max(0, Math.min(h, parseFloat(mxUtils.getValue(this.style, 'dy', this.dy))));
	var dx = Math.max(0, Math.min(w, parseFloat(mxUtils.getValue(this.style, 'dx', this.dx))));
	var notch = Math.max(0, Math.min(w, parseFloat(mxUtils.getValue(this.style, 'notch', this.notch))));
	var arrowHead = Math.max(0, Math.min(h, parseFloat(mxUtils.getValue(this.style, 'arrowHead', this.arrowHead))));

	c.begin();
	c.moveTo(0, 0);
	c.lineTo(notch, 0);
	c.lineTo(notch, h * 0.5 - dy);
	c.lineTo(w - dx, h * 0.5 - dy);
	c.lineTo(w - dx, h * 0.5 - dy - arrowHead);
	c.lineTo(w, h * 0.5);
	c.lineTo(w - dx, h * 0.5 + dy + arrowHead);
	c.lineTo(w - dx, h * 0.5 + dy);
	c.lineTo(notch, h * 0.5 + dy);
	c.lineTo(notch, h);
	c.lineTo(0, h);
	c.close();
	c.fillAndStroke();
};

mxCellRenderer.registerShape(mxShapeArrows2CalloutArrow.prototype.cst.CALLOUT_ARROW, mxShapeArrows2CalloutArrow);

mxShapeArrows2CalloutArrow.prototype.constraints = null;

Graph.handleFactory[mxShapeArrows2CalloutArrow.prototype.cst.CALLOUT_ARROW] = function(state)
{
	var handles = [Graph.createHandle(state, ['dx', 'dy'], function(bounds)
			{
				var arrowHead = Math.max(0, Math.min(bounds.height / 2, parseFloat(mxUtils.getValue(this.state.style, 'arrowHead', this.arrowHead))));
				var dx = Math.max(0, Math.min(bounds.width, parseFloat(mxUtils.getValue(this.state.style, 'dx', this.dx))));
				var dy = Math.max(0, Math.min(bounds.height / 2 - arrowHead, parseFloat(mxUtils.getValue(this.state.style, 'dy', this.dy))));

				return new mxPoint(bounds.x + bounds.width - dx, bounds.y + bounds.height / 2 - dy);
			}, function(bounds, pt)
			{
				this.state.style['dx'] = Math.round(100 * Math.max(0, Math.min(bounds.width - parseFloat(mxUtils.getValue(this.state.style, 'notch', this.notch)), bounds.x + bounds.width - pt.x))) / 100;
				this.state.style['dy'] = Math.round(100 * Math.max(0, Math.min(bounds.height / 2 - parseFloat(mxUtils.getValue(this.state.style, 'arrowHead', this.arrowHead)), bounds.y + bounds.height / 2 - pt.y))) / 100;
				
			})];
			
	var handle2 = Graph.createHandle(state, ['notch'], function(bounds)
			{
				var notch = Math.max(0, Math.min(bounds.width, parseFloat(mxUtils.getValue(this.state.style, 'notch', this.notch))));

				return new mxPoint(bounds.x + notch, bounds.y + bounds.height / 2);
			}, function(bounds, pt)
			{
				this.state.style['notch'] = Math.round(100 * Math.max(0, Math.min(bounds.width - parseFloat(mxUtils.getValue(this.state.style, 'dx', this.dx)), pt.x - bounds.x))) / 100;
			});

	handles.push(handle2);
	
	var handle3 = Graph.createHandle(state, ['arrowHead'], function(bounds)
			{
				var dx = 		Math.max(0, Math.min(bounds.width, parseFloat(mxUtils.getValue(this.state.style, 'dx', this.dx))));
				var dy =        Math.max(0, Math.min(bounds.height / 2, parseFloat(mxUtils.getValue(this.state.style, 'dy', this.dy))));
				var arrowHead = Math.max(0, Math.min(bounds.height / 2, parseFloat(mxUtils.getValue(this.state.style, 'arrowHead', this.arrowHead))));

				return new mxPoint(bounds.x + bounds.width - dx, bounds.y + bounds.height / 2 - dy - arrowHead);
			}, function(bounds, pt)
			{
			this.state.style['arrowHead'] = Math.round(100 * Math.max(0, Math.min(bounds.height / 2 - parseFloat(mxUtils.getValue(this.state.style, 'dy', this.dy)), bounds.y + bounds.height / 2 - parseFloat(mxUtils.getValue(this.state.style, 'dy', this.dy)) - pt.y))) / 100;
			});

	handles.push(handle3);
	
	return handles;
}

//**********************************************************************************************************************************************************
//Bend Arrow
//**********************************************************************************************************************************************************
/**
* Extends mxShape.
*/
function mxShapeArrows2BendArrow(bounds, fill, stroke, strokewidth)
{
	mxShape.call(this);
	this.bounds = bounds;
	this.fill = fill;
	this.stroke = stroke;
	this.strokewidth = (strokewidth != null) ? strokewidth : 1;
	this.dy = 0.5;
	this.dx = 0.5;
	this.notch = 0;
	this.arrowHead = 40;
};

/**
* Extends mxShape.
*/
mxUtils.extend(mxShapeArrows2BendArrow, mxActor);

mxShapeArrows2BendArrow.prototype.cst = {
		BEND_ARROW : 'mxgraph.arrows2.bendArrow'
};

/**
* Function: paintVertexShape
* 
* Paints the vertex shape.
*/
mxShapeArrows2BendArrow.prototype.paintVertexShape = function(c, x, y, w, h)
{
	c.translate(x, y);

	var dy = Math.max(0, Math.min(h, parseFloat(mxUtils.getValue(this.style, 'dy', this.dy))));
	var dx = Math.max(0, Math.min(w, parseFloat(mxUtils.getValue(this.style, 'dx', this.dx))));
	var notch = Math.max(0, Math.min(h, parseFloat(mxUtils.getValue(this.style, 'notch', this.notch))));
	var arrowHead = Math.max(0, Math.min(h, parseFloat(mxUtils.getValue(this.style, 'arrowHead', this.arrowHead))));
	var rounded = mxUtils.getValue(this.style, 'rounded', '0');

	c.begin();
	c.moveTo(w - dx, 0);
	c.lineTo(w, arrowHead * 0.5);
	c.lineTo(w - dx, arrowHead);
	c.lineTo(w - dx, arrowHead / 2 + dy);
	if (rounded == '1')
	{
		c.lineTo(dy * 2.2, arrowHead / 2 + dy);
		c.arcTo(dy * 0.2, dy * 0.2, 0, 0, 0, dy * 2, arrowHead / 2 + dy * 1.2);
	}
	else
	{
		c.lineTo(dy * 2, arrowHead / 2 + dy);
	}
	
	c.lineTo(dy * 2, h);
	c.lineTo(dy, h - notch);
	c.lineTo(0, h);
	
	if (rounded == '1')
	{
		c.lineTo(0, arrowHead / 2 + dy);
		c.arcTo(dy * 2, dy * 2, 0, 0, 1, dy * 2, arrowHead / 2 - dy);
	}
	else
	{
		c.lineTo(0, arrowHead / 2 - dy);
	}

	c.lineTo(w - dx, arrowHead / 2 - dy);
	c.close();
	c.fillAndStroke();
};

mxCellRenderer.registerShape(mxShapeArrows2BendArrow.prototype.cst.BEND_ARROW, mxShapeArrows2BendArrow);

mxShapeArrows2BendArrow.prototype.constraints = null;

Graph.handleFactory[mxShapeArrows2BendArrow.prototype.cst.BEND_ARROW] = function(state)
{
	var handles = [Graph.createHandle(state, ['dx', 'dy'], function(bounds)
			{
				var arrowHead = Math.max(0, Math.min(bounds.height, parseFloat(mxUtils.getValue(this.state.style, 'arrowHead', this.arrowHead))));
				var dx = Math.max(0, Math.min(bounds.width, parseFloat(mxUtils.getValue(this.state.style, 'dx', this.dx))));
				var dy = Math.max(0, Math.min(arrowHead, parseFloat(mxUtils.getValue(this.state.style, 'dy', this.dy))));
		
				return new mxPoint(bounds.x + bounds.width - dx, bounds.y + arrowHead / 2 - dy);
			}, function(bounds, pt)
			{
				this.state.style['dx'] = Math.round(100 * Math.max(0, Math.min(bounds.width - parseFloat(mxUtils.getValue(this.state.style, 'dy', this.dy)) * 2.2, bounds.x + bounds.width - pt.x))) / 100;
				this.state.style['dy'] = Math.round(100 * Math.max(0, Math.min(parseFloat(mxUtils.getValue(this.state.style, 'arrowHead', this.arrowHead)) / 2, bounds.y + parseFloat(mxUtils.getValue(this.state.style, 'arrowHead', this.arrowHead)) / 2 - pt.y))) / 100;
				
			})];
			
	var handle2 = Graph.createHandle(state, ['notch'], function(bounds)
			{
				var arrowHead = Math.max(0, Math.min(bounds.height, parseFloat(mxUtils.getValue(this.state.style, 'arrowHead', this.arrowHead))));
				var dy = Math.max(0, Math.min(arrowHead, parseFloat(mxUtils.getValue(this.state.style, 'dy', this.dy))));
				
				var notch = Math.max(0, Math.min(bounds.height - arrowHead / 2 - dy, parseFloat(mxUtils.getValue(this.state.style, 'notch', this.notch))));

				return new mxPoint(bounds.x + dy, bounds.y + bounds.height - notch);
			}, function(bounds, pt)
			{
				this.state.style['notch'] = Math.round(100 * Math.max(0, Math.min(bounds.height - parseFloat(mxUtils.getValue(this.state.style, 'arrowHead', this.arrowHead)) / 2 - parseFloat(mxUtils.getValue(this.state.style, 'dy', this.dy)), bounds.y + bounds.height - pt.y))) / 100;
			});
	
	handles.push(handle2);

	var handle3 = Graph.createHandle(state, ['arrowHead'], function(bounds)
			{
				var dx = Math.max(0, Math.min(bounds.width, parseFloat(mxUtils.getValue(this.state.style, 'dx', this.dx))));
				var arrowHead = Math.max(0, Math.min(bounds.height, parseFloat(mxUtils.getValue(this.state.style, 'arrowHead', this.arrowHead))));

				return new mxPoint(bounds.x + bounds.width - dx, bounds.y + arrowHead);
			}, function(bounds, pt)
			{
				this.state.style['arrowHead'] = Math.round(100 * Math.max(2 * parseFloat(mxUtils.getValue(this.state.style, 'dy', this.dy)), Math.min(bounds.height, pt.y - bounds.y))) / 100;
			});
	
	handles.push(handle3);

	return handles;
}

//**********************************************************************************************************************************************************
//Bend Double Arrow
//**********************************************************************************************************************************************************
/**
* Extends mxShape.
*/
function mxShapeArrows2BendDoubleArrow(bounds, fill, stroke, strokewidth)
{
	mxShape.call(this);
	this.bounds = bounds;
	this.fill = fill;
	this.stroke = stroke;
	this.strokewidth = (strokewidth != null) ? strokewidth : 1;
	this.dy = 0.5;
	this.dx = 0.5;
	this.notch = 0;
	this.arrowHead = 40;
};

/**
* Extends mxShape.
*/
mxUtils.extend(mxShapeArrows2BendDoubleArrow, mxActor);

mxShapeArrows2BendDoubleArrow.prototype.cst = {
		BEND_DOUBLE_ARROW : 'mxgraph.arrows2.bendDoubleArrow'
};

/**
* Function: paintVertexShape
* 
* Paints the vertex shape.
*/
mxShapeArrows2BendDoubleArrow.prototype.paintVertexShape = function(c, x, y, w, h)
{
	c.translate(x, y);

	var dy = Math.max(0, Math.min(h, parseFloat(mxUtils.getValue(this.style, 'dy', this.dy))));
	var dx = Math.max(0, Math.min(w, parseFloat(mxUtils.getValue(this.style, 'dx', this.dx))));
	var arrowHead = Math.max(0, Math.min(w, parseFloat(mxUtils.getValue(this.style, 'arrowHead', this.arrowHead))));
	var rounded = mxUtils.getValue(this.style, 'rounded', '0');

	c.begin();
	c.moveTo(w - dx, 0);
	c.lineTo(w, arrowHead * 0.5);
	c.lineTo(w - dx, arrowHead);
	c.lineTo(w - dx, arrowHead / 2 + dy);
	if (rounded == '1')
	{
		c.lineTo(arrowHead / 2 + dy * 1.2, arrowHead / 2 + dy);
		c.arcTo(dy * 0.2, dy * 0.2, 0, 0, 0, arrowHead /2 + dy, arrowHead / 2 + dy * 1.2);
	}
	else
	{
		c.lineTo(arrowHead / 2 + dy, arrowHead / 2 + dy);
	}
	
	c.lineTo(arrowHead / 2 + dy, h - dx);
	c.lineTo(arrowHead, h - dx);
	c.lineTo(arrowHead / 2, h);
	c.lineTo(0, h - dx);
	c.lineTo(arrowHead / 2 - dy, h - dx);
	
	if (rounded == '1')
	{
		c.lineTo(arrowHead / 2 - dy, arrowHead / 2 + dy);
		c.arcTo(dy * 2, dy * 2, 0, 0, 1, arrowHead / 2 + dy, arrowHead / 2 - dy);
	}
	else
	{
		c.lineTo(arrowHead / 2 - dy, arrowHead / 2 - dy);
	}

	c.lineTo(w - dx, arrowHead / 2 - dy);
	c.close();
	c.fillAndStroke();
};

mxCellRenderer.registerShape(mxShapeArrows2BendDoubleArrow.prototype.cst.BEND_DOUBLE_ARROW, mxShapeArrows2BendDoubleArrow);

mxShapeArrows2BendDoubleArrow.prototype.constraints = null;

Graph.handleFactory[mxShapeArrows2BendDoubleArrow.prototype.cst.BEND_DOUBLE_ARROW] = function(state)
{
	var handles = [Graph.createHandle(state, ['dx', 'dy'], function(bounds)
			{
				var arrowHead = Math.max(0, Math.min(Math.min(bounds.height, bounds.width) - parseFloat(mxUtils.getValue(this.state.style, 'dx', this.dx)), parseFloat(mxUtils.getValue(this.state.style, 'arrowHead', this.arrowHead))));
				var dx = Math.max(0, Math.min(Math.min(bounds.width, bounds.height) - arrowHead, parseFloat(mxUtils.getValue(this.state.style, 'dx', this.dx))));
				var dy = Math.max(0, Math.min(arrowHead, parseFloat(mxUtils.getValue(this.state.style, 'dy', this.dy))));
		
				return new mxPoint(bounds.x + bounds.width - dx, bounds.y + arrowHead / 2 - dy);
			}, function(bounds, pt)
			{
				this.state.style['dx'] = Math.round(100 * Math.max(0, Math.min(Math.min(bounds.width, bounds.height) - parseFloat(mxUtils.getValue(this.state.style, 'arrowHead', this.arrowHead)), bounds.x + bounds.width - pt.x))) / 100;
				this.state.style['dy'] = Math.round(100 * Math.max(0, Math.min(parseFloat(mxUtils.getValue(this.state.style, 'arrowHead', this.arrowHead)) / 2, bounds.y + parseFloat(mxUtils.getValue(this.state.style, 'arrowHead', this.arrowHead)) / 2 - pt.y))) / 100;
				
			})];
			
	var handle2 = Graph.createHandle(state, ['arrowHead'], function(bounds)
			{
				var dx = Math.max(0, Math.min(bounds.width, parseFloat(mxUtils.getValue(this.state.style, 'dx', this.dx))));
				var arrowHead = Math.max(0, Math.min(Math.min(bounds.height, bounds.width) - dx, parseFloat(mxUtils.getValue(this.state.style, 'arrowHead', this.arrowHead))));

				return new mxPoint(bounds.x + bounds.width - dx, bounds.y + arrowHead);
			}, function(bounds, pt)
			{
				this.state.style['arrowHead'] = Math.round(100 * Math.max(2 * parseFloat(mxUtils.getValue(this.state.style, 'dy', this.dy)), Math.min(Math.min(bounds.height, bounds.width) - parseFloat(mxUtils.getValue(this.state.style, 'dx', this.dx)), pt.y - bounds.y))) / 100;
			});
	
	handles.push(handle2);

	return handles;

}

//**********************************************************************************************************************************************************
//Callout Double Arrow
//**********************************************************************************************************************************************************
/**
* Extends mxShape.
*/
function mxShapeArrows2CalloutDoubleArrow(bounds, fill, stroke, strokewidth)
{
	mxShape.call(this);
	this.bounds = bounds;
	this.fill = fill;
	this.stroke = stroke;
	this.strokewidth = (strokewidth != null) ? strokewidth : 1;
	this.dy = 0.5;
	this.dx = 0.5;
	this.notch = 0;
	this.arrowHead = 0;
};

/**
* Extends mxShape.
*/
mxUtils.extend(mxShapeArrows2CalloutDoubleArrow, mxActor);

mxShapeArrows2CalloutDoubleArrow.prototype.cst = {
		CALLOUT_DOUBLE_ARROW : 'mxgraph.arrows2.calloutDoubleArrow'
};

/**
* Function: paintVertexShape
* 
* Paints the vertex shape.
*/
mxShapeArrows2CalloutDoubleArrow.prototype.paintVertexShape = function(c, x, y, w, h)
{
	c.translate(x, y);

	var dy = Math.max(0, Math.min(h, parseFloat(mxUtils.getValue(this.style, 'dy', this.dy))));
	var dx = Math.max(0, Math.min(w, parseFloat(mxUtils.getValue(this.style, 'dx', this.dx))));
	var notch = Math.max(0, Math.min(w, parseFloat(mxUtils.getValue(this.style, 'notch', this.notch))));
	var arrowHead = Math.max(0, Math.min(h, parseFloat(mxUtils.getValue(this.style, 'arrowHead', this.arrowHead))));

	c.begin();
	c.moveTo(w / 2 - notch, 0);
	c.lineTo(w / 2 + notch, 0);
	c.lineTo(w / 2 + notch, h * 0.5 - dy);
	c.lineTo(w - dx, h * 0.5 - dy);
	c.lineTo(w - dx, h * 0.5 - dy - arrowHead);
	c.lineTo(w, h * 0.5);
	c.lineTo(w - dx, h * 0.5 + dy + arrowHead);
	c.lineTo(w - dx, h * 0.5 + dy);
	c.lineTo(w / 2 + notch, h * 0.5 + dy);
	c.lineTo(w / 2 + notch, h);
	c.lineTo(w / 2 - notch, h);
	c.lineTo(w / 2 - notch, h * 0.5 + dy);
	c.lineTo(dx, h * 0.5 + dy);
	c.lineTo(dx, h * 0.5 + dy + arrowHead);
	c.lineTo(0, h * 0.5);
	c.lineTo(dx, h * 0.5 - dy - arrowHead);
	c.lineTo(dx, h * 0.5 - dy);
	c.lineTo(w / 2 - notch, h * 0.5 - dy);
	c.close();
	c.fillAndStroke();
};

mxCellRenderer.registerShape(mxShapeArrows2CalloutDoubleArrow.prototype.cst.CALLOUT_DOUBLE_ARROW, mxShapeArrows2CalloutDoubleArrow);

mxShapeArrows2CalloutDoubleArrow.prototype.constraints = null;

Graph.handleFactory[mxShapeArrows2CalloutDoubleArrow.prototype.cst.CALLOUT_DOUBLE_ARROW] = function(state)
{
	var handles = [Graph.createHandle(state, ['dx', 'dy'], function(bounds)
			{
				var arrowHead = Math.max(0, Math.min(bounds.height / 2, parseFloat(mxUtils.getValue(this.state.style, 'arrowHead', this.arrowHead))));
				var dx = Math.max(0, Math.min(bounds.width, parseFloat(mxUtils.getValue(this.state.style, 'dx', this.dx))));
				var dy = Math.max(0, Math.min(bounds.height / 2 - arrowHead, parseFloat(mxUtils.getValue(this.state.style, 'dy', this.dy))));

				return new mxPoint(bounds.x + bounds.width - dx, bounds.y + bounds.height / 2 - dy);
			}, function(bounds, pt)
			{
				this.state.style['dx'] = Math.round(100 * Math.max(0, Math.min(bounds.width / 2 - parseFloat(mxUtils.getValue(this.state.style, 'notch', this.notch)), bounds.x + bounds.width - pt.x))) / 100;
				this.state.style['dy'] = Math.round(100 * Math.max(0, Math.min(bounds.height / 2 - parseFloat(mxUtils.getValue(this.state.style, 'arrowHead', this.arrowHead)), bounds.y + bounds.height / 2 - pt.y))) / 100;
				
			})];
			
	var handle2 = Graph.createHandle(state, ['notch'], function(bounds)
			{
				var notch = Math.max(0, Math.min(bounds.width, parseFloat(mxUtils.getValue(this.state.style, 'notch', this.notch))));

				return new mxPoint(bounds.x + bounds.width / 2 + notch, bounds.y + bounds.height / 2);
			}, function(bounds, pt)
			{
				this.state.style['notch'] = Math.round(100 * Math.max(0, Math.min(bounds.width / 2 - parseFloat(mxUtils.getValue(this.state.style, 'dx', this.dx)), pt.x - bounds.x - bounds.width / 2))) / 100;
			});

	handles.push(handle2);
	
	var handle3 = Graph.createHandle(state, ['arrowHead'], function(bounds)
			{
				var dx = 		Math.max(0, Math.min(bounds.width, parseFloat(mxUtils.getValue(this.state.style, 'dx', this.dx))));
				var dy =        Math.max(0, Math.min(bounds.height / 2, parseFloat(mxUtils.getValue(this.state.style, 'dy', this.dy))));
				var arrowHead = Math.max(0, Math.min(bounds.height / 2, parseFloat(mxUtils.getValue(this.state.style, 'arrowHead', this.arrowHead))));

				return new mxPoint(bounds.x + bounds.width - dx, bounds.y + bounds.height / 2 - dy - arrowHead);
			}, function(bounds, pt)
			{
				this.state.style['arrowHead'] = Math.round(100 * Math.max(0, Math.min(bounds.height / 2 - parseFloat(mxUtils.getValue(this.state.style, 'dy', this.dy)), bounds.y + bounds.height / 2 - parseFloat(mxUtils.getValue(this.state.style, 'dy', this.dy)) - pt.y))) / 100;
			});

	handles.push(handle3);
	
	return handles;
}

//**********************************************************************************************************************************************************
//Callout Quad Arrow
//**********************************************************************************************************************************************************
/**
* Extends mxShape.
*/
function mxShapeArrows2CalloutQuadArrow(bounds, fill, stroke, strokewidth)
{
	mxShape.call(this);
	this.bounds = bounds;
	this.fill = fill;
	this.stroke = stroke;
	this.strokewidth = (strokewidth != null) ? strokewidth : 1;
	this.dy = 0.5;
	this.dx = 0.5;
	this.notch = 0;
	this.arrowHead = 0;
};

/**
* Extends mxShape.
*/
mxUtils.extend(mxShapeArrows2CalloutQuadArrow, mxActor);

mxShapeArrows2CalloutQuadArrow.prototype.cst = {
		CALLOUT_QUAD_ARROW : 'mxgraph.arrows2.calloutQuadArrow'
};

/**
* Function: paintVertexShape
* 
* Paints the vertex shape.
*/
mxShapeArrows2CalloutQuadArrow.prototype.paintVertexShape = function(c, x, y, w, h)
{
	c.translate(x, y);

	var dy = Math.max(0, Math.min(h, parseFloat(mxUtils.getValue(this.style, 'dy', this.dy))));
	var dx = Math.max(0, Math.min(w, parseFloat(mxUtils.getValue(this.style, 'dx', this.dx))));
	var notch = Math.max(0, Math.min(w, parseFloat(mxUtils.getValue(this.style, 'notch', this.notch))));
	var arrowHead = Math.max(0, Math.min(h, parseFloat(mxUtils.getValue(this.style, 'arrowHead', this.arrowHead))));

	c.begin();
	c.moveTo(w * 0.5 + dy, h * 0.5 - notch);
	c.lineTo(w * 0.5 + notch, h * 0.5 - notch);
	c.lineTo(w * 0.5 + notch, h * 0.5 - dy);
	c.lineTo(w - dx, h * 0.5 - dy);
	c.lineTo(w - dx, h * 0.5 - dy - arrowHead);
	c.lineTo(w, h * 0.5);
	c.lineTo(w - dx, h * 0.5 + dy + arrowHead);
	c.lineTo(w - dx, h * 0.5 + dy);
	c.lineTo(w * 0.5 + notch, h * 0.5 + dy);
	c.lineTo(w * 0.5 + notch, h * 0.5 + notch);
	c.lineTo(w * 0.5 + dy, h * 0.5 + notch);
	c.lineTo(w * 0.5 + dy, h - dx);
	c.lineTo(w * 0.5 + dy + arrowHead, h - dx);
	c.lineTo(w * 0.5, h);
	c.lineTo(w * 0.5 - dy - arrowHead, h - dx);
	c.lineTo(w * 0.5 - dy, h - dx);
	c.lineTo(w * 0.5 - dy, h * 0.5 + notch);
	c.lineTo(w * 0.5 - notch, h * 0.5 + notch);
	c.lineTo(w * 0.5 - notch, h * 0.5 + dy);
	c.lineTo(dx, h * 0.5 + dy);
	c.lineTo(dx, h * 0.5 + dy + arrowHead);
	c.lineTo(0, h * 0.5);
	c.lineTo(dx, h * 0.5 - dy - arrowHead);
	c.lineTo(dx, h * 0.5 - dy);
	c.lineTo(w * 0.5 - notch, h * 0.5 - dy);
	c.lineTo(w * 0.5 - notch, h * 0.5 - notch);
	c.lineTo(w * 0.5 - dy, h * 0.5 - notch);
	c.lineTo(w * 0.5 - dy, dx);
	c.lineTo(w * 0.5 - dy - arrowHead, dx);
	c.lineTo(w * 0.5, 0);
	c.lineTo(w * 0.5 + dy + arrowHead, dx);
	c.lineTo(w * 0.5 + dy, dx);
	c.close();
	c.fillAndStroke();
};

mxCellRenderer.registerShape(mxShapeArrows2CalloutQuadArrow.prototype.cst.CALLOUT_QUAD_ARROW, mxShapeArrows2CalloutQuadArrow);

mxShapeArrows2CalloutQuadArrow.prototype.constraints = null;

Graph.handleFactory[mxShapeArrows2CalloutQuadArrow.prototype.cst.CALLOUT_QUAD_ARROW] = function(state)
{
	var handles = [Graph.createHandle(state, ['dx', 'dy'], function(bounds)
			{
				var arrowHead = Math.max(0, Math.min(bounds.height / 2, parseFloat(mxUtils.getValue(this.state.style, 'arrowHead', this.arrowHead))));
				var dx = Math.max(0, Math.min(bounds.width, parseFloat(mxUtils.getValue(this.state.style, 'dx', this.dx))));
				var dy = Math.max(0, Math.min(bounds.height, parseFloat(mxUtils.getValue(this.state.style, 'dy', this.dy))));

				return new mxPoint(bounds.x + bounds.width - dx, bounds.y + bounds.height / 2 - dy);
			}, function(bounds, pt)
			{
				this.state.style['dx'] = Math.round(100 * Math.max(0, Math.min(Math.min(bounds.width, bounds.height) / 2 - Math.max(parseFloat(mxUtils.getValue(this.state.style, 'notch', this.notch)), parseFloat(mxUtils.getValue(this.state.style, 'arrowHead', this.arrowHead))), bounds.x + bounds.width - pt.x))) / 100;
				this.state.style['dy'] = Math.round(100 * Math.max(0, Math.min(parseFloat(mxUtils.getValue(this.state.style, 'notch', this.notch)), bounds.y + bounds.height / 2 - pt.y))) / 100;
				
			})];
			
	var handle2 = Graph.createHandle(state, ['notch'], function(bounds)
			{
				var notch = Math.max(parseFloat(mxUtils.getValue(this.state.style, 'dy', this.dy)), Math.min(Math.min(bounds.width, bounds.height), parseFloat(mxUtils.getValue(this.state.style, 'notch', this.notch))));

				return new mxPoint(bounds.x + bounds.width / 2 + notch, bounds.y + bounds.height / 2);
			}, function(bounds, pt)
			{
				this.state.style['notch'] = Math.round(100 * Math.max(parseFloat(mxUtils.getValue(this.state.style, 'dy', this.dy)), Math.min(Math.min(bounds.width, bounds.height) / 2 - parseFloat(mxUtils.getValue(this.state.style, 'dx', this.dx)), pt.x - bounds.x - bounds.width / 2))) / 100;
			});

	handles.push(handle2);
	
	var handle3 = Graph.createHandle(state, ['arrowHead'], function(bounds)
			{
				var dx = 		Math.max(0, Math.min(bounds.width, parseFloat(mxUtils.getValue(this.state.style, 'dx', this.dx))));
				var dy =        Math.max(0, Math.min(bounds.height / 2, parseFloat(mxUtils.getValue(this.state.style, 'dy', this.dy))));
				var arrowHead = Math.max(0, Math.min(bounds.height / 2, parseFloat(mxUtils.getValue(this.state.style, 'arrowHead', this.arrowHead))));

				return new mxPoint(bounds.x + bounds.width - dx, bounds.y + bounds.height / 2 - dy - arrowHead);
			}, function(bounds, pt)
			{
				this.state.style['arrowHead'] = Math.round(100 * Math.max(0, Math.min(bounds.height / 2 - parseFloat(mxUtils.getValue(this.state.style, 'dy', this.dy)), bounds.y + bounds.height / 2 - pt.y))) / 100;
			});

	handles.push(handle3);
	
	return handles;
}

//**********************************************************************************************************************************************************
//Callout Double 90 Arrow
//**********************************************************************************************************************************************************
/**
* Extends mxShape.
*/
function mxShapeArrows2CalloutDouble90Arrow(bounds, fill, stroke, strokewidth)
{
	mxShape.call(this);
	this.bounds = bounds;
	this.fill = fill;
	this.stroke = stroke;
	this.strokewidth = (strokewidth != null) ? strokewidth : 1;
	this.dy1 = 0.5;
	this.dx1 = 0.5;
	this.dx2 = 0;
	this.dy2 = 0;
	this.arrowHead = 0;
};

/**
* Extends mxShape.
*/
mxUtils.extend(mxShapeArrows2CalloutDouble90Arrow, mxActor);

mxShapeArrows2CalloutDouble90Arrow.prototype.cst = {
		CALLOUT_DOUBLE_90_ARROW : 'mxgraph.arrows2.calloutDouble90Arrow'
};

/**
* Function: paintVertexShape
* 
* Paints the vertex shape.
*/
mxShapeArrows2CalloutDouble90Arrow.prototype.paintVertexShape = function(c, x, y, w, h)
{
	c.translate(x, y);

	var dy1 = Math.max(0, Math.min(h, parseFloat(mxUtils.getValue(this.style, 'dy1', this.dy1))));
	var dx1 = Math.max(0, Math.min(w, parseFloat(mxUtils.getValue(this.style, 'dx1', this.dx1))));
	var dx2 = Math.max(0, Math.min(w, parseFloat(mxUtils.getValue(this.style, 'dx2', this.dx2))));
	var dy2 = Math.max(0, Math.min(w, parseFloat(mxUtils.getValue(this.style, 'dy2', this.dy2))));
	var arrowHead = Math.max(0, Math.min(h, parseFloat(mxUtils.getValue(this.style, 'arrowHead', this.arrowHead))));

	c.begin();
	c.moveTo(0, 0);
	c.lineTo(dx2, 0);
	c.lineTo(dx2, dy2 * 0.5 - dy1);
	c.lineTo(w - dx1, dy2 * 0.5 - dy1);
	c.lineTo(w - dx1, dy2 * 0.5 - dy1 - arrowHead);
	c.lineTo(w, dy2 * 0.5);
	c.lineTo(w - dx1, dy2 * 0.5 + dy1 + arrowHead);
	c.lineTo(w - dx1, dy2 * 0.5 + dy1);
	c.lineTo(dx2, dy2 * 0.5 + dy1);
	c.lineTo(dx2, dy2);
	c.lineTo(dx2 / 2 + dy1, dy2);
	c.lineTo(dx2 / 2 + dy1, h - dx1);
	c.lineTo(dx2 / 2 + dy1 + arrowHead, h - dx1);
	c.lineTo(dx2 / 2, h);
	c.lineTo(dx2 / 2 - dy1 - arrowHead, h - dx1);
	c.lineTo(dx2 / 2 - dy1, h - dx1);
	c.lineTo(dx2 / 2 - dy1, dy2);
	c.lineTo(0, dy2);
	c.close();
	c.fillAndStroke();
};

mxCellRenderer.registerShape(mxShapeArrows2CalloutDouble90Arrow.prototype.cst.CALLOUT_DOUBLE_90_ARROW, mxShapeArrows2CalloutDouble90Arrow);

mxShapeArrows2CalloutDouble90Arrow.prototype.constraints = null;

Graph.handleFactory[mxShapeArrows2CalloutDouble90Arrow.prototype.cst.CALLOUT_DOUBLE_90_ARROW] = function(state)
{
	var handles = [Graph.createHandle(state, ['dx1', 'dy1'], function(bounds)
			{
				var arrowHead = Math.max(0, Math.min(parseFloat(mxUtils.getValue(this.state.style, 'dy2', this.dy2)) / 2, parseFloat(mxUtils.getValue(this.state.style, 'arrowHead', this.arrowHead))));
				var dx1 = Math.max(0, Math.min(bounds.width - parseFloat(mxUtils.getValue(this.state.style, 'dx2', this.dx2)), parseFloat(mxUtils.getValue(this.state.style, 'dx1', this.dx1))));
				var dy1 = Math.max(0, Math.min(parseFloat(mxUtils.getValue(this.state.style, 'dy2', this.dy2)) / 2 - arrowHead, parseFloat(mxUtils.getValue(this.state.style, 'dy1', this.dy1))));

				return new mxPoint(bounds.x + bounds.width - dx1, bounds.y + parseFloat(mxUtils.getValue(this.state.style, 'dy2', this.dy2)) / 2 - dy1);
			}, function(bounds, pt)
			{
				this.state.style['dx1'] = Math.round(100 * Math.max(0, Math.min(bounds.width - parseFloat(mxUtils.getValue(this.state.style, 'dx2', this.dx2)), bounds.height - parseFloat(mxUtils.getValue(this.state.style, 'dy2', this.dy2)), bounds.x + bounds.width - pt.x))) / 100;
				this.state.style['dy1'] = Math.round(100 * Math.max(0, Math.min(parseFloat(mxUtils.getValue(this.state.style, 'dy2', this.dy2)) / 2 - parseFloat(mxUtils.getValue(this.state.style, 'arrowHead', this.arrowHead)), bounds.y + parseFloat(mxUtils.getValue(this.state.style, 'dy2', this.dy2)) / 2 - pt.y))) / 100;
				
			})];
			
	var handle2 = Graph.createHandle(state, ['dx2', 'dy2'], function(bounds)
			{
				var dx2 = Math.max(parseFloat(mxUtils.getValue(this.state.style, 'dy1', this.dy1)) + parseFloat(mxUtils.getValue(this.state.style, 'arrowHead', this.arrowHead)), Math.min(bounds.width - parseFloat(mxUtils.getValue(this.state.style, 'dx1', this.dx1)), parseFloat(mxUtils.getValue(this.state.style, 'dx2', this.dx2))));
				var dy2 = Math.max(parseFloat(mxUtils.getValue(this.state.style, 'dy1', this.dy1)) + parseFloat(mxUtils.getValue(this.state.style, 'arrowHead', this.arrowHead)), Math.min(bounds.height - parseFloat(mxUtils.getValue(this.state.style, 'dy1', this.dy1)), parseFloat(mxUtils.getValue(this.state.style, 'dy2', this.dy2))));

				return new mxPoint(bounds.x + dx2, bounds.y + dy2);
			}, function(bounds, pt)
			{
				this.state.style['dx2'] = Math.round(100 * Math.max(parseFloat(mxUtils.getValue(this.state.style, 'dy1', this.dy1)) + parseFloat(mxUtils.getValue(this.state.style, 'arrowHead', this.arrowHead)), Math.min(bounds.width - parseFloat(mxUtils.getValue(this.state.style, 'dx1', this.dx1)), pt.x - bounds.x))) / 100;
				this.state.style['dy2'] = Math.round(100 * Math.max(parseFloat(mxUtils.getValue(this.state.style, 'dy1', this.dy1)) + parseFloat(mxUtils.getValue(this.state.style, 'arrowHead', this.arrowHead)), Math.min(bounds.height - parseFloat(mxUtils.getValue(this.state.style, 'dx1', this.dx1)), pt.y - bounds.y))) / 100;
			});

	handles.push(handle2);
	
	var handle3 = Graph.createHandle(state, ['arrowHead'], function(bounds)
			{
				var dx1 = 		Math.max(0, Math.min(bounds.width, parseFloat(mxUtils.getValue(this.state.style, 'dx1', this.dx1))));
				var dy1 =        Math.max(0, Math.min(parseFloat(mxUtils.getValue(this.state.style, 'dy2', this.dy2)) / 2, parseFloat(mxUtils.getValue(this.state.style, 'dy1', this.dy1))));
				var arrowHead = Math.max(0, Math.min(parseFloat(mxUtils.getValue(this.state.style, 'dy2', this.dy2)) / 2, parseFloat(mxUtils.getValue(this.state.style, 'arrowHead', this.arrowHead))));

				return new mxPoint(bounds.x + bounds.width - dx1, bounds.y + parseFloat(mxUtils.getValue(this.state.style, 'dy2', this.dy2)) / 2 - dy1 - arrowHead);
			}, function(bounds, pt)
			{
				this.state.style['arrowHead'] = Math.round(100 * Math.max(0, Math.min(parseFloat(mxUtils.getValue(this.state.style, 'dy2', this.dy2)) / 2 - parseFloat(mxUtils.getValue(this.state.style, 'dy1', this.dy1)), bounds.y + parseFloat(mxUtils.getValue(this.state.style, 'dy2', this.dy2)) / 2 - parseFloat(mxUtils.getValue(this.state.style, 'dy1', this.dy1)) - pt.y))) / 100;
			});

	handles.push(handle3);
	
	return handles;
}

//**********************************************************************************************************************************************************
//Quad Arrow
//**********************************************************************************************************************************************************
/**
* Extends mxShape.
*/
function mxShapeArrows2QuadArrow(bounds, fill, stroke, strokewidth)
{
	mxShape.call(this);
	this.bounds = bounds;
	this.fill = fill;
	this.stroke = stroke;
	this.strokewidth = (strokewidth != null) ? strokewidth : 1;
	this.dy = 0.5;
	this.dx = 0.5;
	this.notch = 0;
	this.arrowHead = 0;
};

/**
* Extends mxShape.
*/
mxUtils.extend(mxShapeArrows2QuadArrow, mxActor);

mxShapeArrows2QuadArrow.prototype.cst = {
		QUAD_ARROW : 'mxgraph.arrows2.quadArrow'
};

/**
* Function: paintVertexShape
* 
* Paints the vertex shape.
*/
mxShapeArrows2QuadArrow.prototype.paintVertexShape = function(c, x, y, w, h)
{
	c.translate(x, y);

	var dy = Math.max(0, Math.min(h, parseFloat(mxUtils.getValue(this.style, 'dy', this.dy))));
	var dx = Math.max(0, Math.min(w, parseFloat(mxUtils.getValue(this.style, 'dx', this.dx))));
	var arrowHead = Math.max(0, Math.min(h, parseFloat(mxUtils.getValue(this.style, 'arrowHead', this.arrowHead))));

	c.begin();
	c.moveTo(w * 0.5 + dy, h * 0.5 - dy);
	c.lineTo(w - dx, h * 0.5 - dy);
	c.lineTo(w - dx, h * 0.5 - dy - arrowHead);
	c.lineTo(w, h * 0.5);
	c.lineTo(w - dx, h * 0.5 + dy + arrowHead);
	c.lineTo(w - dx, h * 0.5 + dy);
	c.lineTo(w * 0.5 + dy, h * 0.5 + dy);
	c.lineTo(w * 0.5 + dy, h - dx);
	c.lineTo(w * 0.5 + dy + arrowHead, h - dx);
	c.lineTo(w * 0.5, h);
	c.lineTo(w * 0.5 - dy - arrowHead, h - dx);
	c.lineTo(w * 0.5 - dy, h - dx);
	c.lineTo(w * 0.5 - dy, h * 0.5 + dy);
	c.lineTo(dx, h * 0.5 + dy);
	c.lineTo(dx, h * 0.5 + dy + arrowHead);
	c.lineTo(0, h * 0.5);
	c.lineTo(dx, h * 0.5 - dy - arrowHead);
	c.lineTo(dx, h * 0.5 - dy);
	c.lineTo(w * 0.5 - dy, h * 0.5 - dy);
	c.lineTo(w * 0.5 - dy, dx);
	c.lineTo(w * 0.5 - dy - arrowHead, dx);
	c.lineTo(w * 0.5, 0);
	c.lineTo(w * 0.5 + dy + arrowHead, dx);
	c.lineTo(w * 0.5 + dy, dx);
	c.close();
	c.fillAndStroke();
};

mxCellRenderer.registerShape(mxShapeArrows2QuadArrow.prototype.cst.QUAD_ARROW, mxShapeArrows2QuadArrow);

mxShapeArrows2QuadArrow.prototype.constraints = null;

Graph.handleFactory[mxShapeArrows2QuadArrow.prototype.cst.QUAD_ARROW] = function(state)
{
	var handles = [Graph.createHandle(state, ['dx', 'dy'], function(bounds)
			{
				var arrowHead = Math.max(0, Math.min(bounds.height / 2, parseFloat(mxUtils.getValue(this.state.style, 'arrowHead', this.arrowHead))));
				var dx = Math.max(0, Math.min(bounds.width, parseFloat(mxUtils.getValue(this.state.style, 'dx', this.dx))));
				var dy = Math.max(0, Math.min(bounds.height / 2, parseFloat(mxUtils.getValue(this.state.style, 'dy', this.dy))));

				return new mxPoint(bounds.x + bounds.width - dx, bounds.y + bounds.height / 2 - dy);
			}, function(bounds, pt)
			{
				this.state.style['dx'] = Math.round(100 * Math.max(0, Math.min(Math.min(bounds.width, bounds.height) / 2 - parseFloat(mxUtils.getValue(this.state.style, 'dy', this.dy)) - parseFloat(mxUtils.getValue(this.state.style, 'arrowHead', this.arrowHead)), bounds.x + bounds.width - pt.x))) / 100;
				this.state.style['dy'] = Math.round(100 * Math.max(0, Math.min(bounds.height / 2 - parseFloat(mxUtils.getValue(this.state.style, 'arrowHead', this.arrowHead)), bounds.y + bounds.height / 2 - pt.y))) / 100;
				
			})];
			
	var handle2 = Graph.createHandle(state, ['arrowHead'], function(bounds)
			{
				var dx = 		Math.max(0, Math.min(bounds.width, parseFloat(mxUtils.getValue(this.state.style, 'dx', this.dx))));
				var dy =        Math.max(0, Math.min(bounds.height / 2, parseFloat(mxUtils.getValue(this.state.style, 'dy', this.dy))));
				var arrowHead = Math.max(0, Math.min(bounds.height / 2, parseFloat(mxUtils.getValue(this.state.style, 'arrowHead', this.arrowHead))));

				return new mxPoint(bounds.x + bounds.width - dx, bounds.y + bounds.height / 2 - dy - arrowHead);
			}, function(bounds, pt)
			{
				this.state.style['arrowHead'] = Math.round(100 * Math.max(0, Math.min(bounds.height / 2 - parseFloat(mxUtils.getValue(this.state.style, 'dy', this.dy)), bounds.y + bounds.height / 2 - pt.y))) / 100;
			});

	handles.push(handle2);
	
	return handles;
}

//**********************************************************************************************************************************************************
//Triad Arrow
//**********************************************************************************************************************************************************
/**
* Extends mxShape.
*/
function mxShapeArrows2TriadArrow(bounds, fill, stroke, strokewidth)
{
	mxShape.call(this);
	this.bounds = bounds;
	this.fill = fill;
	this.stroke = stroke;
	this.strokewidth = (strokewidth != null) ? strokewidth : 1;
	this.dy = 0.5;
	this.dx = 0.5;
	this.arrowHead = 0;
};

/**
* Extends mxShape.
*/
mxUtils.extend(mxShapeArrows2TriadArrow, mxActor);

mxShapeArrows2TriadArrow.prototype.cst = {
		TRIAD_ARROW : 'mxgraph.arrows2.triadArrow'
};

/**
* Function: paintVertexShape
* 
* Paints the vertex shape.
*/
mxShapeArrows2TriadArrow.prototype.paintVertexShape = function(c, x, y, w, h)
{
	c.translate(x, y);

	var dy = Math.max(0, Math.min(h, parseFloat(mxUtils.getValue(this.style, 'dy', this.dy))));
	var dx = Math.max(0, Math.min(w, parseFloat(mxUtils.getValue(this.style, 'dx', this.dx))));
	var arrowHead = Math.max(0, Math.min(h, parseFloat(mxUtils.getValue(this.style, 'arrowHead', this.arrowHead))));

	c.begin();
	c.moveTo(w * 0.5 + arrowHead * 0.5 - dy, h - arrowHead + dy);
	c.lineTo(w - dx, h - arrowHead + dy);
	c.lineTo(w - dx, h - arrowHead);
	c.lineTo(w, h - arrowHead * 0.5);
	c.lineTo(w - dx, h);
	c.lineTo(w - dx, h - dy);
	c.lineTo(dx, h - dy);
	c.lineTo(dx, h);
	c.lineTo(0, h - arrowHead * 0.5);
	c.lineTo(dx, h - arrowHead);
	c.lineTo(dx, h - arrowHead + dy);
	c.lineTo(w * 0.5 - arrowHead * 0.5 + dy, h - arrowHead + dy);
	c.lineTo(w * 0.5 - arrowHead * 0.5 + dy, dx);
	c.lineTo(w * 0.5 - arrowHead * 0.5, dx);
	c.lineTo(w * 0.5, 0);
	c.lineTo(w * 0.5 + arrowHead * 0.5, dx);
	c.lineTo(w * 0.5 + arrowHead * 0.5 - dy, dx);
	c.close();
	c.fillAndStroke();
};

mxCellRenderer.registerShape(mxShapeArrows2TriadArrow.prototype.cst.TRIAD_ARROW, mxShapeArrows2TriadArrow);

mxShapeArrows2TriadArrow.prototype.constraints = null;

Graph.handleFactory[mxShapeArrows2TriadArrow.prototype.cst.TRIAD_ARROW] = function(state)
{
	var handles = [Graph.createHandle(state, ['dx', 'dy'], function(bounds)
			{
				var arrowHead = Math.max(0, Math.min(bounds.height / 2, parseFloat(mxUtils.getValue(this.state.style, 'arrowHead', this.arrowHead))));
				var dx = Math.max(0, Math.min(bounds.width, parseFloat(mxUtils.getValue(this.state.style, 'dx', this.dx))));
				var dy = Math.max(0, Math.min(arrowHead, parseFloat(mxUtils.getValue(this.state.style, 'dy', this.dy))));

				return new mxPoint(bounds.x + bounds.width - dx, bounds.y + bounds.height - dy);
			}, function(bounds, pt)
			{
				this.state.style['dx'] = Math.round(100 * Math.max(0, Math.min(Math.min(bounds.height - parseFloat(mxUtils.getValue(this.state.style, 'arrowHead', this.arrowHead)), bounds.width / 2 - parseFloat(mxUtils.getValue(this.state.style, 'arrowHead', this.arrowHead)) / 2), bounds.x + bounds.width - pt.x))) / 100;
				this.state.style['dy'] = Math.round(100 * Math.max(0, Math.min(parseFloat(mxUtils.getValue(this.state.style, 'arrowHead', this.arrowHead)) / 2, bounds.y + bounds.height - pt.y))) / 100;
				
			})];
			
	var handle2 = Graph.createHandle(state, ['arrowHead'], function(bounds)
			{
				var dx = 		Math.max(0, Math.min(bounds.width, parseFloat(mxUtils.getValue(this.state.style, 'dx', this.dx))));
				var dy =        Math.max(0, Math.min(bounds.height / 2, parseFloat(mxUtils.getValue(this.state.style, 'dy', this.dy))));
				var arrowHead = Math.max(0, Math.min(bounds.height, parseFloat(mxUtils.getValue(this.state.style, 'arrowHead', this.arrowHead))));

				return new mxPoint(bounds.x + bounds.width - dx, bounds.y + bounds.height - arrowHead);
			}, function(bounds, pt)
			{
				this.state.style['arrowHead'] = Math.round(100 * Math.max(2 * parseFloat(mxUtils.getValue(this.state.style, 'dy', this.dy)), Math.min(bounds.height - parseFloat(mxUtils.getValue(this.state.style, 'dx', this.dx)), bounds.width - parseFloat(mxUtils.getValue(this.state.style, 'dx', this.dx)) * 2, bounds.y + bounds.height - pt.y))) / 100;
			});

	handles.push(handle2);
	
	return handles;
}

//**********************************************************************************************************************************************************
//Tailed Arrow
//**********************************************************************************************************************************************************
/**
* Extends mxShape.
*/
function mxShapeArrows2TailedArrow(bounds, fill, stroke, strokewidth)
{
	mxShape.call(this);
	this.bounds = bounds;
	this.fill = fill;
	this.stroke = stroke;
	this.strokewidth = (strokewidth != null) ? strokewidth : 1;
	this.dy = 0.5;
	this.dx = 0.5;
	this.notch = 0;
	this.arrowHead = 0;
};

/**
* Extends mxShape.
*/
mxUtils.extend(mxShapeArrows2TailedArrow, mxActor);

mxShapeArrows2TailedArrow.prototype.cst = {
		TAILED_ARROW : 'mxgraph.arrows2.tailedArrow'
};

/**
* Function: paintVertexShape
* 
* Paints the vertex shape.
*/
mxShapeArrows2TailedArrow.prototype.paintVertexShape = function(c, x, y, w, h)
{
	c.translate(x, y);

	var dy1 = Math.max(0, Math.min(h, parseFloat(mxUtils.getValue(this.style, 'dy1', this.dy1))));
	var dx1 = Math.max(0, Math.min(w, parseFloat(mxUtils.getValue(this.style, 'dx1', this.dx1))));
	var dy2 = Math.max(0, Math.min(h, parseFloat(mxUtils.getValue(this.style, 'dy2', this.dy2))));
	var dx2 = Math.max(0, Math.min(w, parseFloat(mxUtils.getValue(this.style, 'dx2', this.dx2))));
	var notch = Math.max(0, Math.min(w, parseFloat(mxUtils.getValue(this.style, 'notch', this.notch))));
	var arrowHead = Math.max(0, Math.min(h, parseFloat(mxUtils.getValue(this.style, 'arrowHead', this.arrowHead))));

	var x2 = 0;
	
	if (dy2 != 0)
	{
		x2 = dx2 + dy2 * (dy2 - dy1) / dy2;
	}

	c.begin();
	c.moveTo(0, h * 0.5 - dy2);
	c.lineTo(dx2, h * 0.5 - dy2);
	c.lineTo(x2, h * 0.5 - dy1);
	c.lineTo(w - dx1, h * 0.5 - dy1);
	c.lineTo(w - dx1, h * 0.5 - dy1 - arrowHead);
	c.lineTo(w, h * 0.5);
	c.lineTo(w - dx1, h * 0.5 + dy1 + arrowHead);
	c.lineTo(w - dx1, h * 0.5 + dy1);
	c.lineTo(x2, h * 0.5 + dy1);
	c.lineTo(dx2, h * 0.5 + dy2);
	c.lineTo(0, h * 0.5 + dy2);
	c.lineTo(notch, h * 0.5);
	c.close();
	c.fillAndStroke();
};

mxCellRenderer.registerShape(mxShapeArrows2TailedArrow.prototype.cst.TAILED_ARROW, mxShapeArrows2TailedArrow);

mxShapeArrows2TailedArrow.prototype.constraints = null;

Graph.handleFactory[mxShapeArrows2TailedArrow.prototype.cst.TAILED_ARROW] = function(state)
{
	var handles = [Graph.createHandle(state, ['dx1', 'dy1'], function(bounds)
			{
				var arrowHead = Math.max(0, Math.min(bounds.height / 2, parseFloat(mxUtils.getValue(this.state.style, 'arrowHead', this.arrowHead))));
				var dx1 = Math.max(0, Math.min(bounds.width, parseFloat(mxUtils.getValue(this.state.style, 'dx1', this.dx1))));
				var dy1 = Math.max(0, Math.min(bounds.height / 2 - arrowHead, parseFloat(mxUtils.getValue(this.state.style, 'dy1', this.dy1))));

				return new mxPoint(bounds.x + bounds.width - dx1, bounds.y + bounds.height / 2 - dy1);
			}, function(bounds, pt)
			{
				this.state.style['dx1'] = Math.round(100 * Math.max(0, Math.min(bounds.width - parseFloat(mxUtils.getValue(this.state.style, 'notch', this.notch)), bounds.width - parseFloat(mxUtils.getValue(this.state.style, 'dx2', this.dx2)), bounds.x + bounds.width - pt.x))) / 100;
				this.state.style['dy1'] = Math.round(100 * Math.max(0, Math.min(bounds.height / 2 - parseFloat(mxUtils.getValue(this.state.style, 'arrowHead', this.arrowHead)), parseFloat(mxUtils.getValue(this.state.style, 'dy2', this.dy2)), bounds.y + bounds.height / 2 - pt.y))) / 100;
				
			})];
			
	var handle2 = Graph.createHandle(state, ['notch'], function(bounds)
			{
				var notch = Math.max(0, Math.min(bounds.width, parseFloat(mxUtils.getValue(this.state.style, 'notch', this.notch))));

				return new mxPoint(bounds.x + notch, bounds.y + bounds.height / 2);
			}, function(bounds, pt)
			{
				this.state.style['notch'] = Math.round(100 * Math.max(0, Math.min(bounds.width - parseFloat(mxUtils.getValue(this.state.style, 'dx1', this.dx1)), parseFloat(mxUtils.getValue(this.state.style, 'dx2', this.dx2)), pt.x - bounds.x))) / 100;
			});

	handles.push(handle2);
	
	var handle3 = Graph.createHandle(state, ['arrowHead'], function(bounds)
			{
				var dx1 = 		Math.max(0, Math.min(bounds.width, parseFloat(mxUtils.getValue(this.state.style, 'dx1', this.dx1))));
				var dy1 =        Math.max(0, Math.min(bounds.height / 2, parseFloat(mxUtils.getValue(this.state.style, 'dy1', this.dy1))));
				var arrowHead = Math.max(0, Math.min(bounds.height / 2, parseFloat(mxUtils.getValue(this.state.style, 'arrowHead', this.arrowHead))));

				return new mxPoint(bounds.x + bounds.width - dx1, bounds.y + bounds.height / 2 - dy1 - arrowHead);
			}, function(bounds, pt)
			{
				this.state.style['arrowHead'] = Math.round(100 * Math.max(0, Math.min(bounds.height / 2 - parseFloat(mxUtils.getValue(this.state.style, 'dy1', this.dy1)), bounds.y + bounds.height / 2 - pt.y))) / 100;
			});

	handles.push(handle3);
	
	var handle4 = Graph.createHandle(state, ['dx2', 'dy2'], function(bounds)
			{
				var dx2 = Math.max(0, Math.min(bounds.width, parseFloat(mxUtils.getValue(this.state.style, 'dx2', this.dx2))));
				var dy2 = Math.max(0, Math.min(bounds.height / 2, parseFloat(mxUtils.getValue(this.state.style, 'dy2', this.dy2))));

				return new mxPoint(bounds.x + dx2, bounds.y + bounds.height / 2 - dy2);
			}, function(bounds, pt)
			{
				this.state.style['dx2'] = Math.round(100 * Math.max(parseFloat(mxUtils.getValue(this.state.style, 'notch', this.notch)), Math.min(bounds.width - parseFloat(mxUtils.getValue(this.state.style, 'dx1', this.dx1)) - parseFloat(mxUtils.getValue(this.state.style, 'dy2', this.dy2)) + parseFloat(mxUtils.getValue(this.state.style, 'dy1', this.dy1)) - 1, pt.x - bounds.x))) / 100;
				this.state.style['dy2'] = Math.round(100 * Math.max(parseFloat(mxUtils.getValue(this.state.style, 'dy1', this.dy1)), Math.min(bounds.height / 2, bounds.y + bounds.height / 2 - pt.y))) / 100;
				
			});

	handles.push(handle4);

	return handles;
}

//**********************************************************************************************************************************************************
//Tailed Arrow with Notch
//**********************************************************************************************************************************************************
/**
* Extends mxShape.
*/
function mxShapeArrows2TailedNotchedArrow(bounds, fill, stroke, strokewidth)
{
	mxShape.call(this);
	this.bounds = bounds;
	this.fill = fill;
	this.stroke = stroke;
	this.strokewidth = (strokewidth != null) ? strokewidth : 1;
	this.dy = 0.5;
	this.dx = 0.5;
	this.notch = 0;
	this.arrowHead = 0;
};

/**
* Extends mxShape.
*/
mxUtils.extend(mxShapeArrows2TailedNotchedArrow, mxActor);

mxShapeArrows2TailedNotchedArrow.prototype.cst = {
		TAILED_NOTCHED_ARROW : 'mxgraph.arrows2.tailedNotchedArrow'
};

/**
* Function: paintVertexShape
* 
* Paints the vertex shape.
*/
mxShapeArrows2TailedNotchedArrow.prototype.paintVertexShape = function(c, x, y, w, h)
{
	c.translate(x, y);

	var dy1 = Math.max(0, Math.min(h, parseFloat(mxUtils.getValue(this.style, 'dy1', this.dy1))));
	var dx1 = Math.max(0, Math.min(w, parseFloat(mxUtils.getValue(this.style, 'dx1', this.dx1))));
	var dy2 = Math.max(0, Math.min(h, parseFloat(mxUtils.getValue(this.style, 'dy2', this.dy2))));
	var dx2 = Math.max(0, Math.min(w, parseFloat(mxUtils.getValue(this.style, 'dx2', this.dx2))));
	var notch = Math.max(0, Math.min(w, parseFloat(mxUtils.getValue(this.style, 'notch', this.notch))));
	var arrowHead = Math.max(0, Math.min(h, parseFloat(mxUtils.getValue(this.style, 'arrowHead', this.arrowHead))));

	var x2 = 0;
	
	if (dy2 != 0)
	{
		x2 = dx2 + notch * (dy2 - dy1) / dy2;
	}

	c.begin();
	c.moveTo(0, h * 0.5 - dy2);
	c.lineTo(dx2, h * 0.5 - dy2);
	c.lineTo(x2, h * 0.5 - dy1);
	c.lineTo(w - dx1, h * 0.5 - dy1);
	c.lineTo(w - dx1, h * 0.5 - dy1 - arrowHead);
	c.lineTo(w, h * 0.5);
	c.lineTo(w - dx1, h * 0.5 + dy1 + arrowHead);
	c.lineTo(w - dx1, h * 0.5 + dy1);
	c.lineTo(x2, h * 0.5 + dy1);
	c.lineTo(dx2, h * 0.5 + dy2);
	c.lineTo(0, h * 0.5 + dy2);
	c.lineTo(notch, h * 0.5);
	c.close();
	c.fillAndStroke();
};

mxCellRenderer.registerShape(mxShapeArrows2TailedNotchedArrow.prototype.cst.TAILED_NOTCHED_ARROW, mxShapeArrows2TailedNotchedArrow);

mxShapeArrows2TailedNotchedArrow.prototype.constraints = null;

Graph.handleFactory[mxShapeArrows2TailedNotchedArrow.prototype.cst.TAILED_NOTCHED_ARROW] = function(state)
{
	var handles = [Graph.createHandle(state, ['dx1', 'dy1'], function(bounds)
			{
				var arrowHead = Math.max(0, Math.min(bounds.height / 2, parseFloat(mxUtils.getValue(this.state.style, 'arrowHead', this.arrowHead))));
				var dx1 = Math.max(0, Math.min(bounds.width, parseFloat(mxUtils.getValue(this.state.style, 'dx1', this.dx1))));
				var dy1 = Math.max(0, Math.min(bounds.height / 2 - arrowHead, parseFloat(mxUtils.getValue(this.state.style, 'dy1', this.dy1))));

				return new mxPoint(bounds.x + bounds.width - dx1, bounds.y + bounds.height / 2 - dy1);
			}, function(bounds, pt)
			{
				this.state.style['dx1'] = Math.round(100 * Math.max(0, Math.min(bounds.width - parseFloat(mxUtils.getValue(this.state.style, 'notch', this.notch))- parseFloat(mxUtils.getValue(this.state.style, 'dx2', this.dx2)), bounds.x + bounds.width - pt.x))) / 100;
				this.state.style['dy1'] = Math.round(100 * Math.max(0, Math.min(bounds.height / 2 - parseFloat(mxUtils.getValue(this.state.style, 'arrowHead', this.arrowHead)), parseFloat(mxUtils.getValue(this.state.style, 'dy2', this.dy2)), bounds.y + bounds.height / 2 - pt.y))) / 100;
				
			})];
			
	var handle2 = Graph.createHandle(state, ['notch'], function(bounds)
			{
				var notch = Math.max(0, Math.min(bounds.width, parseFloat(mxUtils.getValue(this.state.style, 'notch', this.notch))));

				return new mxPoint(bounds.x + notch, bounds.y + bounds.height / 2);
			}, function(bounds, pt)
			{
				this.state.style['notch'] = Math.round(100 * Math.max(0, Math.min(bounds.width - parseFloat(mxUtils.getValue(this.state.style, 'dx1', this.dx1)), pt.x - bounds.x))) / 100;
			});

	handles.push(handle2);
	
	var handle3 = Graph.createHandle(state, ['arrowHead'], function(bounds)
			{
				var dx1 = 		Math.max(0, Math.min(bounds.width, parseFloat(mxUtils.getValue(this.state.style, 'dx1', this.dx1))));
				var dy1 =        Math.max(0, Math.min(bounds.height / 2, parseFloat(mxUtils.getValue(this.state.style, 'dy1', this.dy1))));
				var arrowHead = Math.max(0, Math.min(bounds.height / 2, parseFloat(mxUtils.getValue(this.state.style, 'arrowHead', this.arrowHead))));

				return new mxPoint(bounds.x + bounds.width - dx1, bounds.y + bounds.height / 2 - dy1 - arrowHead);
			}, function(bounds, pt)
			{
				this.state.style['arrowHead'] = Math.round(100 * Math.max(0, Math.min(bounds.height / 2 - parseFloat(mxUtils.getValue(this.state.style, 'dy1', this.dy1)), bounds.y + bounds.height / 2 - pt.y))) / 100;
			});

	handles.push(handle3);
	
	var handle4 = Graph.createHandle(state, ['dx2', 'dy2'], function(bounds)
			{
				var dx2 = Math.max(0, Math.min(bounds.width, parseFloat(mxUtils.getValue(this.state.style, 'dx2', this.dx2))));
				var dy2 = Math.max(0, Math.min(bounds.height / 2, parseFloat(mxUtils.getValue(this.state.style, 'dy2', this.dy2))));

				return new mxPoint(bounds.x + dx2, bounds.y + bounds.height / 2 - dy2);
			}, function(bounds, pt)
			{
				this.state.style['dx2'] = Math.round(100 * Math.max(0, Math.min(bounds.width - parseFloat(mxUtils.getValue(this.state.style, 'notch', this.notch)) - parseFloat(mxUtils.getValue(this.state.style, 'dx1', this.dx1)), pt.x - bounds.x))) / 100;
				this.state.style['dy2'] = Math.round(100 * Math.max(parseFloat(mxUtils.getValue(this.state.style, 'dy1', this.dy1)), Math.min(bounds.height / 2, bounds.y + bounds.height / 2 - pt.y))) / 100;
				
			});

	handles.push(handle4);

	return handles;
}

//**********************************************************************************************************************************************************
//Striped Arrow
//**********************************************************************************************************************************************************
/**
* Extends mxShape.
*/
function mxShapeArrows2StripedArrow(bounds, fill, stroke, strokewidth)
{
	mxShape.call(this);
	this.bounds = bounds;
	this.fill = fill;
	this.stroke = stroke;
	this.strokewidth = (strokewidth != null) ? strokewidth : 1;
	this.dy = 0.5;
	this.dx = 0.5;
	this.notch = 0;
};

/**
* Extends mxShape.
*/
mxUtils.extend(mxShapeArrows2StripedArrow, mxActor);

mxShapeArrows2StripedArrow.prototype.cst = {
		STRIPED_ARROW : 'mxgraph.arrows2.stripedArrow'
};

/**
* Function: paintVertexShape
* 
* Paints the vertex shape.
*/
mxShapeArrows2StripedArrow.prototype.paintVertexShape = function(c, x, y, w, h)
{
	c.translate(x, y);

	var dy = h * 0.5 * Math.max(0, Math.min(1, parseFloat(mxUtils.getValue(this.style, 'dy', this.dy))));
	var dx = Math.max(0, Math.min(w, parseFloat(mxUtils.getValue(this.style, 'dx', this.dx))));
	var notch = Math.max(0, Math.min(w, parseFloat(mxUtils.getValue(this.style, 'notch', this.notch))));

	c.begin();
	c.moveTo(notch, dy);
	c.lineTo(w - dx, dy);
	c.lineTo(w - dx, 0);
	c.lineTo(w, h * 0.5);
	c.lineTo(w - dx, h);
	c.lineTo(w - dx, h - dy);
	c.lineTo(notch, h - dy);
	c.close();
	c.moveTo(0, h - dy);
	c.lineTo(notch * 0.16, h - dy);
	c.lineTo(notch * 0.16, dy);
	c.lineTo(0, dy);
	c.close();
	c.moveTo(notch * 0.32, h - dy);
	c.lineTo(notch * 0.8, h - dy);
	c.lineTo(notch * 0.8, dy);
	c.lineTo(notch * 0.32, dy);
	c.close();
	c.fillAndStroke();
	
};

mxCellRenderer.registerShape(mxShapeArrows2StripedArrow.prototype.cst.STRIPED_ARROW, mxShapeArrows2StripedArrow);

mxShapeArrows2StripedArrow.prototype.constraints = null;

Graph.handleFactory[mxShapeArrows2StripedArrow.prototype.cst.STRIPED_ARROW] = function(state)
{
	var handles = [Graph.createHandle(state, ['dx', 'dy'], function(bounds)
			{
				var dx = Math.max(0, Math.min(bounds.width, parseFloat(mxUtils.getValue(this.state.style, 'dx', this.dx))));
				var dy = Math.max(0, Math.min(1, parseFloat(mxUtils.getValue(this.state.style, 'dy', this.dy))));

				return new mxPoint(bounds.x + bounds.width - dx, bounds.y + dy * bounds.height / 2);
			}, function(bounds, pt)
			{
				this.state.style['dx'] = Math.round(100 * Math.max(0, Math.min(bounds.width - parseFloat(mxUtils.getValue(this.state.style, 'notch', this.notch)), bounds.x + bounds.width - pt.x))) / 100;
				this.state.style['dy'] = Math.round(100 * Math.max(0, Math.min(1, (((pt.y - bounds.y) / bounds.height) * 2)))) / 100;
			})];
			
	var handle2 = Graph.createHandle(state, ['notch'], function(bounds)
			{
				var notch = Math.max(0, Math.min(bounds.width, parseFloat(mxUtils.getValue(this.state.style, 'notch', this.notch))));

				return new mxPoint(bounds.x + notch, bounds.y + bounds.height / 2);
			}, function(bounds, pt)
			{
				this.state.style['notch'] = Math.round(100 * Math.max(0, Math.min(bounds.width - parseFloat(mxUtils.getValue(this.state.style, 'dx', this.dx)), pt.x - bounds.x))) / 100;
			});
	
	handles.push(handle2);
	
	return handles;

}

//**********************************************************************************************************************************************************
//Jump-In Arrow
//**********************************************************************************************************************************************************
/**
* Extends mxShape.
*/
function mxShapeArrows2JumpInArrow(bounds, fill, stroke, strokewidth)
{
	mxShape.call(this);
	this.bounds = bounds;
	this.fill = fill;
	this.stroke = stroke;
	this.strokewidth = (strokewidth != null) ? strokewidth : 1;
	this.dy = 0.5;
	this.dx = 0.5;
	this.arrowHead = 40;
};

/**
* Extends mxShape.
*/
mxUtils.extend(mxShapeArrows2JumpInArrow, mxActor);

mxShapeArrows2JumpInArrow.prototype.cst = {
		JUMP_IN_ARROW : 'mxgraph.arrows2.jumpInArrow'
};

/**
* Function: paintVertexShape
* 
* Paints the vertex shape.
*/
mxShapeArrows2JumpInArrow.prototype.paintVertexShape = function(c, x, y, w, h)
{
	c.translate(x, y);

	var dy = Math.max(0, Math.min(h, parseFloat(mxUtils.getValue(this.style, 'dy', this.dy))));
	var dx = Math.max(0, Math.min(w, parseFloat(mxUtils.getValue(this.style, 'dx', this.dx))));
	var arrowHead = Math.max(0, Math.min(h, parseFloat(mxUtils.getValue(this.style, 'arrowHead', this.arrowHead))));

	c.begin();
	c.moveTo(w - dx, 0);
	c.lineTo(w, arrowHead * 0.5);
	c.lineTo(w - dx, arrowHead);
	c.lineTo(w - dx, arrowHead / 2 + dy);
	c.arcTo(w - dx, h - arrowHead / 2 - dy, 0, 0, 0, 0, h);
	c.arcTo(w - dx, h - arrowHead / 2 + dy, 0, 0, 1, w - dx, arrowHead / 2 - dy);
	c.close();
	c.fillAndStroke();
};

mxCellRenderer.registerShape(mxShapeArrows2JumpInArrow.prototype.cst.JUMP_IN_ARROW, mxShapeArrows2JumpInArrow);

mxShapeArrows2JumpInArrow.prototype.constraints = null;

Graph.handleFactory[mxShapeArrows2JumpInArrow.prototype.cst.JUMP_IN_ARROW] = function(state)
{
	var handles = [Graph.createHandle(state, ['dx', 'dy'], function(bounds)
			{
				var arrowHead = Math.max(0, Math.min(bounds.height, parseFloat(mxUtils.getValue(this.state.style, 'arrowHead', this.arrowHead))));
				var dx = Math.max(0, Math.min(bounds.width, parseFloat(mxUtils.getValue(this.state.style, 'dx', this.dx))));
				var dy = Math.max(0, Math.min(arrowHead, parseFloat(mxUtils.getValue(this.state.style, 'dy', this.dy))));
		
				return new mxPoint(bounds.x + bounds.width - dx, bounds.y + arrowHead / 2 - dy);
			}, function(bounds, pt)
			{
				this.state.style['dx'] = Math.round(100 * Math.max(0, Math.min(bounds.width, bounds.x + bounds.width - pt.x))) / 100;
				this.state.style['dy'] = Math.round(100 * Math.max(0, Math.min(parseFloat(mxUtils.getValue(this.state.style, 'arrowHead', this.arrowHead)) / 2, bounds.y + parseFloat(mxUtils.getValue(this.state.style, 'arrowHead', this.arrowHead)) / 2 - pt.y))) / 100;
				
			})];
			
	var handle2 = Graph.createHandle(state, ['arrowHead'], function(bounds)
			{
				var dx = Math.max(0, Math.min(bounds.width, parseFloat(mxUtils.getValue(this.state.style, 'dx', this.dx))));
				var arrowHead = Math.max(0, Math.min(bounds.height, parseFloat(mxUtils.getValue(this.state.style, 'arrowHead', this.arrowHead))));

				return new mxPoint(bounds.x + bounds.width - dx, bounds.y + arrowHead);
			}, function(bounds, pt)
			{
				this.state.style['arrowHead'] = Math.round(100 * Math.max(2 * parseFloat(mxUtils.getValue(this.state.style, 'dy', this.dy)), Math.min(bounds.height, pt.y - bounds.y))) / 100;
			});
	
	handles.push(handle2);

	return handles;
}

//**********************************************************************************************************************************************************
//U Turn Arrow
//**********************************************************************************************************************************************************
/**
* Extends mxShape.
*/
function mxShapeArrows2UTurnArrow(bounds, fill, stroke, strokewidth)
{
	mxShape.call(this);
	this.bounds = bounds;
	this.fill = fill;
	this.stroke = stroke;
	this.strokewidth = (strokewidth != null) ? strokewidth : 1;
	this.dy = 0.5;
	this.dx = 0.5;
	this.arrowHead = 40;
};

/**
* Extends mxShape.
*/
mxUtils.extend(mxShapeArrows2UTurnArrow, mxActor);

mxShapeArrows2UTurnArrow.prototype.cst = {
		U_TURN_ARROW : 'mxgraph.arrows2.uTurnArrow'
};

/**
* Function: paintVertexShape
* 
* Paints the vertex shape.
*/
mxShapeArrows2UTurnArrow.prototype.paintVertexShape = function(c, x, y, w, h)
{
	c.translate(x, y);

	var dy = Math.max(0, Math.min(h, parseFloat(mxUtils.getValue(this.style, 'dy', this.dy))));
	var arrowHead = Math.max(0, Math.min(h, parseFloat(mxUtils.getValue(this.style, 'arrowHead', this.arrowHead))));

	var dx = (h - arrowHead / 2 + dy) / 2;

	var dx2 = Math.max(0, parseFloat(mxUtils.getValue(this.style, 'dx2', this.dx2)));
	
	c.begin();
	c.moveTo(dx, 0);
	c.lineTo(dx + dx2, arrowHead * 0.5);
	c.lineTo(dx, arrowHead);
	c.lineTo(dx, arrowHead / 2 + dy);
	c.arcTo(dx - 2 * dy, dx - 2 * dy, 0, 0, 0, dx, h - 2 * dy);
	c.lineTo(Math.max(w, dx), h - 2 * dy);
	c.lineTo(Math.max(w, dx), h);
	c.lineTo(dx, h);
	c.arcTo(dx, dx, 0, 0, 1, dx, arrowHead / 2 - dy);
	c.close();
	c.fillAndStroke();
};

mxCellRenderer.registerShape(mxShapeArrows2UTurnArrow.prototype.cst.U_TURN_ARROW, mxShapeArrows2UTurnArrow);

mxShapeArrows2UTurnArrow.prototype.constraints = null;

Graph.handleFactory[mxShapeArrows2UTurnArrow.prototype.cst.U_TURN_ARROW] = function(state)
{
	var handles = [Graph.createHandle(state, ['dy'], function(bounds)
			{
				var arrowHead = Math.max(0, Math.min(bounds.height, parseFloat(mxUtils.getValue(this.state.style, 'arrowHead', this.arrowHead))));
				var dy = Math.max(0, Math.min(arrowHead, parseFloat(mxUtils.getValue(this.state.style, 'dy', this.dy))));
				var dx = (bounds.height - arrowHead / 2 + dy) / 2;

				return new mxPoint(bounds.x + dx, bounds.y + arrowHead / 2 - dy);
			}, function(bounds, pt)
			{
				this.state.style['dy'] = Math.round(100 * Math.max(0, Math.min(parseFloat(mxUtils.getValue(this.state.style, 'arrowHead', this.arrowHead)) / 2, bounds.y + parseFloat(mxUtils.getValue(this.state.style, 'arrowHead', this.arrowHead)) / 2 - pt.y))) / 100;
				
			})];

	var handle2 = Graph.createHandle(state, ['dx2'], function(bounds)
			{
				var arrowHead = Math.max(0, Math.min(bounds.height, parseFloat(mxUtils.getValue(this.state.style, 'arrowHead', this.arrowHead))));
				var dy = Math.max(0, Math.min(arrowHead, parseFloat(mxUtils.getValue(this.state.style, 'dy', this.dy))));
				var dx = (bounds.height - arrowHead / 2 + dy) / 2;
				
				var dx2 = Math.max(0, Math.min(bounds.width - dx, parseFloat(mxUtils.getValue(this.state.style, 'dx2', this.dx2))));

				return new mxPoint(bounds.x + dx + dx2, bounds.y + arrowHead / 2);
			}, function(bounds, pt)
			{
				var arrowHead = Math.max(0, Math.min(bounds.height, parseFloat(mxUtils.getValue(this.state.style, 'arrowHead', this.arrowHead))));
				var dy = Math.max(0, Math.min(arrowHead, parseFloat(mxUtils.getValue(this.state.style, 'dy', this.dy))));
				var dx = (bounds.height - arrowHead / 2 + dy) / 2;
				this.state.style['dx2'] = Math.round(100 * Math.max(0, Math.min(Math.max(bounds.width, dx), pt.x - bounds.x - dx))) / 100;
			});
	
	handles.push(handle2);

	var handle3 = Graph.createHandle(state, ['arrowHead'], function(bounds)
			{
				var arrowHead = Math.max(0, Math.min(bounds.height, parseFloat(mxUtils.getValue(this.state.style, 'arrowHead', this.arrowHead))));
				var dy = Math.max(0, Math.min(arrowHead, parseFloat(mxUtils.getValue(this.state.style, 'dy', this.dy))));
				var dx = (bounds.height - arrowHead / 2 + dy) / 2;

				return new mxPoint(bounds.x + dx, bounds.y + arrowHead);
			}, function(bounds, pt)
			{
				this.state.style['arrowHead'] = Math.round(100 * Math.max(2 * parseFloat(mxUtils.getValue(this.state.style, 'dy', this.dy)), Math.min(bounds.height / 2, pt.y - bounds.y))) / 100;
			});
	
	handles.push(handle3);

	return handles;
}
