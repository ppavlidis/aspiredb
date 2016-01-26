/*
 * The aspiredb project
 * 
 * Copyright (c) 2013 University of British Columbia
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

/**
 * Highlighting regions in the chromosome.
 * 
 * @param {CanvasRenderingContext2D}
 *           ctx
 * @param {number}
 *           leftX
 * @param {ChromosomeValueObject}
 *           chromosomeData
 * @param {ChromosomeLayer}
 *           chromosomeLayer
 * @constructor
 */
var IdeogramCursorLayer = function(ctx, selectionCtx, leftX, chromosomeData, chromosomeLayer) {
   this.ctx = ctx;
   this.selectionCtx = selectionCtx;
   this.xPosition = leftX;
   this.chromosomeData = chromosomeData;
   this.chromosomeLayer = chromosomeLayer;
   this.isSelectionMode = false;

   this.cursorBackground = new Background(ctx);
   this.selectionBackground = new Background(selectionCtx);

   /**
    * @public
    * @returns {number}
    */
   this.getLeftX = function() {
      return getLeftX();
   };

   /**
    * @private
    * @returns {number}
    */
   var getLeftX = function() {
      return leftX;
   };

   /**
    * Helper class
    * 
    * @constructor
    */
   function Background(ctx) {
      var boxWidth;
      var boxHeight;
      var x;
      var y;

      this.clearActiveArea = function() {
    	  // clear active box
    	  ctx.clearRect(x, y, boxWidth, boxHeight);
      };
      
      this.setActiveArea = function(leftX, topY, width, height) {
    	  // Save active box for faster clearing
          boxHeight = height;
          boxWidth = width;
          y = topY;
          x = leftX;
      };

      this.clear = function() {
    	  // clear entire background
    	  ctx.clearRect(0, 0, ctx.canvas.width, ctx.canvas.height);
      };
   }

   var me = this;
   /**
    * @type {{}}
    */
   this.selection = {
      /* GenomicRange */
      selectedRange : null,
      start : null,
      end : null,

      getSelectedRange : function() {
         return this.selectedRange;
      },

      setSelectedRange : function(/* GenomicRange */selectedRange) {
         this.selectedRange = selectedRange;
      },

      getTop : function() {
         return Math.min( this.start, this.end );
      },

      getBottom : function() {
         return Math.max( this.start, this.end );
      },

      setStart : function(start) {
         this.start = start;
      },

      setCurrent : function(current) {
         this.end = current;
      },

      setEnd : function(end) {
         this.end = end;

         if ( Math.abs( this.start - end ) <= 1 ) {
            me.clearSelection();
         } else {
            var selectedStartBase;
            var selectedEndBase;

            selectedStartBase = chromosomeLayer.convertToBaseCoordinate( this.getTop() );
            selectedEndBase = chromosomeLayer.convertToBaseCoordinate( this.getBottom() );

            var selectedChromosome = chromosomeData.name;
            this.setSelectedRange( {
               chromosome : selectedChromosome,
               baseStart : selectedStartBase,
               baseEnd : selectedEndBase
            } );
         }
      },
      render : function() {
         // Render selection
         me.selectionCtx.fillStyle = "rgba(0,0,255,0.1)";
         var x = getLeftX();
         var y = this.getTop();
         var yBottom = this.getBottom();
         var width = 29;
         var height =  yBottom - y;
         me.selectionCtx.fillRect( x, y, width, height );

         var bBox = me.renderCursor( y, me.selectionCtx );
         me.renderCursor( yBottom, me.selectionCtx );
                 
         // return bounding box for drawings
         return {leftX:x, topY:bBox.topY, width:bBox.width, height: height + bBox.height }
      },
      clear : function() {
         this.start = 0;
         this.end = 0;
      }
   };
};

/**
 * @returns {GenomicRange}
 */
IdeogramCursorLayer.prototype.getSelectedRange = function() {
   return this.selection.getSelectedRange();
};

IdeogramCursorLayer.prototype.renderCursor = function(y, ctx) {
   var me = this;
   var getBandName = function(y) {
      var base = me.chromosomeLayer.convertToBaseCoordinate( y );
      for ( var bandName in me.chromosomeData.bands) {
         var band = me.chromosomeData.bands[bandName];
         if ( band.start < base && band.end > base ) {
            return band.name;
         }
      }
      return "";
   };
   
   var redLineWidth = 30;
   var fontSizeInPixels = 12;
   var x = this.getLeftX();

   ctx.fillStyle = "red";
   ctx.fillRect( x, y, redLineWidth - 1, 1 );

   var cursorLabel = this.chromosomeData.name + ":" + getBandName( y );

//   this.ctx.strokeStyle = "black";
//   this.ctx.strokeText( cursorLabel, this.getLeftX() + 30, y );
   
   var textX = x + redLineWidth;
   
   ctx.font= fontSizeInPixels + 'px sans-serif';
   
   ctx.strokeStyle = 'dark grey';
   ctx.fillStyle = "white";

	// get rid of some edge effects
	ctx.miterLimit = 2;
	ctx.lineJoin = 'round';
	
	// draw an outline, then filled
	ctx.lineWidth = 3;	
	
	var textAlignBaseline = ctx.textAlign;
    ctx.textBaseline = "middle";
    
	
	ctx.strokeText(cursorLabel, textX, y);
//	this.ctx.lineWidth = 4;
	ctx.fillText(cursorLabel, textX, y);
	
	ctx.textBaseline = textAlignBaseline;
	
	// approximate bounding box of text, 50% buffer
	var aWidth = 1.5*(redLineWidth + ctx.measureText(cursorLabel).width);
	var aHeight = 1.5*fontSizeInPixels;
	
	return {leftX:x, topY:y - aHeight/2 , width:aWidth, height: aHeight}
};

IdeogramCursorLayer.prototype.drawCursor = function(y) {
   var me = this;
   function drawSelection(y) {
      me.selectionBackground.clearActiveArea();

      me.selection.setCurrent( y );
      var boundingBox = me.selection.render();
      me.selectionBackground.setActiveArea(boundingBox.leftX, boundingBox.topY, boundingBox.width, boundingBox.height);
   }

   if ( this.isSelectionMode ) {
      drawSelection( y );
   } else {
      this.cursorBackground.clearActiveArea();
      var boundingBox = this.renderCursor( y, this.ctx );
      me.cursorBackground.setActiveArea(boundingBox.leftX, boundingBox.topY, boundingBox.width, boundingBox.height);

   }
};

IdeogramCursorLayer.prototype.clearCursor = function() {
   if ( this.isSelectionMode && this.selection.getSelectedRange() == null ) {
      this.clearSelection();
   } else {
//      this.cursorBackground.restore();
      this.cursorBackground.clear();
   }
};

IdeogramCursorLayer.prototype.startSelection = function(y) {
   this.cursorBackground.clear();

   this.isSelectionMode = true;

   this.selection.setSelectedRange( null );
   this.selection.setStart( y );
};

IdeogramCursorLayer.prototype.clearSelection = function() {
   //this.selectionBackground.restore();
   this.selectionBackground.clear();

   this.selection.setSelectedRange( null );
   this.selection.clear();
   this.isSelectionMode = false;
};

IdeogramCursorLayer.prototype.finishSelection = function(y) {
   if ( this.isSelectionMode ) {
      this.isSelectionMode = false;
      this.selection.setEnd( y );
      //this.cursorBackground.clear();
   }
};
