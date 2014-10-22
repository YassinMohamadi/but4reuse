package org.but4reuse.utils.workbench;

import java.io.File;
import java.net.URI;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;

/**
 * Workbench Utils
 * 
 * @author jabier.martinez
 */
public class WorkbenchUtils {

	/**
	 * Get active editor of a given id
	 * @param editorId
	 * @return the editor or null if not found
	 */
	public static IEditorPart getActiveEditorOfAGivenId(String editorId) {
		IEditorReference[] editorReferences = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getEditorReferences();
		for (int i = 0; i < editorReferences.length; i++) {
			IEditorReference iEditorReference = editorReferences[i];
			if (iEditorReference.getId().equals(editorId)) {
				IEditorPart editorPart = iEditorReference.getEditor(true);
				IWorkbenchPage iwpage = editorPart.getSite().getPage();
				if (iwpage.isPartVisible(editorPart)) {
					return iEditorReference.getEditor(true);
				}
			}
		}
		return null;
	}
	
	/**
	 * Try to force to show a view
	 * @param viewId
	 * @return
	 */
	public static IViewPart forceShowView(String viewId){
		try {
			return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(viewId);
		} catch (PartInitException e) {
			return null;
		}
	}
	
	/**
	 * Refresh all workspace
	 */
	public static void refreshAllWorkspace(){
		try {
			ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Open a file in its default editor
	 * @param file
	 */
	public static void openInEditor(IFile file){
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IEditorDescriptor desc = PlatformUI.getWorkbench().getEditorRegistry().getDefaultEditor(file.getName());
		try {
			page.openEditor(new FileEditorInput(file), desc.getId());
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * try to get the ifile from a file that is supposed to be in the workspace
	 * @param ifile
	 * @return
	 */
	public static IFile getIFileFromFile(File file){
		IWorkspace workspace= ResourcesPlugin.getWorkspace();    
		IPath location= Path.fromOSString(file.getAbsolutePath()); 
		IFile ifile= workspace.getRoot().getFileForLocation(location);
		return ifile;
	}
	
	/**
	 * get File from IResource
	 * @param iresource (including IFile)
	 * @return File
	 */
	public static File getFileFromIResource(IResource resource){
		return resource.getRawLocation().makeAbsolute().toFile();
	}
	
	/**
	 * Open the editor at the location of the given marker.
	 * @param marker
	 */
	public static void openInEditor(IMarker marker) {
		IWorkbenchPage page = getActivePage();
		try {
			IDE.openEditor(page, marker);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}
	
	public static IWorkbenchPage getActivePage() {
		IWorkbench wb = PlatformUI.getWorkbench();
		   IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
		   return win.getActivePage();
	}
	
	/**
	 * Convert IFile to URI
	 * @param uri
	 * @return the ifile or null
	 */
	public static IFile getIFileFromURI(URI uri) {
		// TODO It is possible that IFile is not in the workspace, check real Location
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		URI rootUri = root.getLocationURI();
		uri = rootUri.relativize(uri);
		IPath path = new Path(uri.getPath());
		path = path.removeFirstSegments(1);
		return root.getFile(path);
	}
	

}
