/* $Id: UMLSignalContextListModel.java 17896 2010-01-12 21:36:11Z linus $
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    bobtarling
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2004-2006 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.core.propertypanels.ui;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.argouml.i18n.Translator;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.uml.ui.AbstractActionAddModelElement2;
import org.argouml.uml.ui.AbstractActionRemoveElement;

/**
 * The model for the listbox showing the contexts of a signal.
 *
 * @author mvw@tigris.org
 */
class UMLSignalContextListModel extends UMLModelElementListModel {

    /**
     * The class uid
     */
    private static final long serialVersionUID = 2002737769556890982L;
    
    /**
     * The constructor.
     */
    public UMLSignalContextListModel(Object modelElement) {
        super("context",
                modelElement.getClass(),
            new ActionAddContextSignal(), 
            new ActionRemoveContextSignal());
        setTarget(modelElement);
    }

    /*
     * @see org.argouml.uml.ui.UMLModelElementListModel2#buildModelList()
     */
    protected void buildModelList() {
        if (getTarget() != null) {
            setAllElements(Model.getFacade().getContexts(getTarget()));
        }
    }

    /*
     * @see org.argouml.uml.ui.UMLModelElementListModel2#isValidElement(java.lang.Object)
     */
    protected boolean isValidElement(Object element) {
        return Model.getFacade().isABehavioralFeature(element)
                && Model.getFacade().getContexts(getTarget()).contains(
                        element);
    }

    /**
     * This Action removes a Context from a Signal.
     * 
     * @author Michiel
     */
    private static class ActionRemoveContextSignal extends AbstractActionRemoveElement {

        /**
         * The serial version.
         */
        private static final long serialVersionUID = -3345844954130000669L;

        /**
         * Construct an Action which removes a Context from a Signal.
         */
        public ActionRemoveContextSignal() {
            super(Translator.localize("menu.popup.remove"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            super.actionPerformed(e);
            Object context = getObjectToRemove(); 
            if (context != null) {
                Object signal = getTarget();
                if (Model.getFacade().isASignal(signal)) {
                    Model.getCommonBehaviorHelper().removeContext(signal, context);
                }
            }
        }
    }
    
    /**
     * This action adds a context to a signal.
     *
     * @author mvw@tigris.org
     */
    private static class ActionAddContextSignal extends AbstractActionAddModelElement2 {

        /**
         * The constructor.
         */
        public ActionAddContextSignal() {
            super();
        }

        protected List getChoices() {
            List ret = new ArrayList();
            Object model =
                ProjectManager.getManager().getCurrentProject().getModel();
            if (getTarget() != null) {
                ret.addAll(Model.getModelManagementHelper()
                        .getAllBehavioralFeatures(model));
            }
            return ret;
        }

        protected List getSelected() {
            List ret = new ArrayList();
            ret.addAll(Model.getFacade().getContexts(getTarget()));
            return ret;
        }

        protected String getDialogTitle() {
            return Translator.localize("dialog.title.add-contexts");
        }

        protected void doIt(Collection selected) {
            Model.getCommonBehaviorHelper().setContexts(getTarget(), selected);
        }
    }
}
