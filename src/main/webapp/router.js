/**
 * Simple URL-based Router for Expense Reimbursement System
 * Pure vanilla JavaScript implementation
 */
class Router {
  constructor() {
    this.routes = new Map();
    this.currentRoute = null;
    this.notFoundCallback = null;
    this.guards = [];
    this.basePath = '/ers';
    
    // Initialize router
    this.init();
  }

  /**
   * Initialize router and handle browser navigation events
   */
  init() {
    // Initialize user session before handling routes
    if (typeof initializeUserSession === 'function') {
      initializeUserSession();
    }
    
    // Handle initial page load
    window.addEventListener('load', () => {
      this.handleRoute();
    });

    // Handle browser back/forward buttons
    window.addEventListener('popstate', () => {
      this.handleRoute();
    });
  }

  /**
   * Add a new route to the router
   * @param {string} path - URL path (e.g., '/login')
   * @param {Function} component - Page constructor function
   * @param {Object} options - Route options (requiresAuth, roles, etc.)
   */
  addRoute(path, component, options = {}) {
    const normalizedPath = this.normalizePath(path);
    this.routes.set(normalizedPath, {
      path: normalizedPath,
      component,
      options: {
        requiresAuth: false,
        roles: [],
        title: '',
        ...options
      }
    });
  }

  /**
   * Add route guard for authentication/authorization
   * @param {Function} guard - Guard function that returns true/false
   */
  addGuard(guard) {
    this.guards.push(guard);
  }

  /**
   * Set 404/not found handler
   * @param {Function} callback - Function to call when route not found
   */
  setNotFound(callback) {
    this.notFoundCallback = callback;
  }

  /**
   * Navigate to a specific route
   * @param {string} path - Target path
   * @param {Object} state - Optional state object
   */
  navigate(path, state = {}) {
    const normalizedPath = this.normalizePath(path);
    const fullUrl = this.basePath + normalizedPath;
    
    // Update browser history
    history.pushState(state, '', fullUrl);
    
    // Handle the route
    this.handleRoute();
  }

  /**
   * Handle current route from URL
   */
  handleRoute() {
    const currentPath = this.getCurrentPath();
    const route = this.findRoute(currentPath);
    
    if (route) {
      // Check guards before navigation
      if (this.checkGuards(route)) {
        this.renderRoute(route);
      } else {
        console.warn('Route access denied by guards');
        this.navigate('/login');
      }
    } else {
      // Handle 404
      if (this.notFoundCallback) {
        this.notFoundCallback(currentPath);
      } else {
        console.warn(`Route not found: ${currentPath}`);
        this.navigate('/'); // fallback to home
      }
    }
  }

  /**
   * Find matching route for current path
   * @param {string} path - Current path
   * @returns {Object|null} Route object or null
   */
  findRoute(path) {
    // Exact match first
    if (this.routes.has(path)) {
      return this.routes.get(path);
    }
    
    // Try to find a route with parameters
    for (const [routePath, route] of this.routes) {
      if (this.pathMatches(routePath, path)) {
        return {
          ...route,
          params: this.extractParams(routePath, path)
        };
      }
    }
    
    return null;
  }

  /**
   * Check if route path matches current path with parameters
   * @param {string} routePath - Route pattern
   * @param {string} currentPath - Current path
   * @returns {boolean} - Whether paths match
   */
  pathMatches(routePath, currentPath) {
    // Simple exact match for now
    // TODO: Add parameter support like '/tickets/:id'
    return routePath === currentPath;
  }

  /**
   * Extract parameters from route
   * @param {string} routePath - Route pattern
   * @param {string} currentPath - Current path
   * @returns {Object} - Route parameters
   */
  extractParams(routePath, currentPath) {
    // TODO: Implement parameter extraction
    return {};
  }

  /**
   * Check all guards for route access
   * @param {Object} route - Route object
   * @returns {boolean} - Whether access is allowed
   */
  checkGuards(route) {
    return this.guards.every(guard => guard(route));
  }

  /**
   * Render the route component
   * @param {Object} route - Route object
   */
  renderRoute(route) {
    // Clean up current page if exists
    this.cleanup();
    
    try {
      // Update document title
      if (route.options.title) {
        document.title = route.options.title;
      }
      
      // Create and render new page
      const page = new route.component(route.params || {});
      page.render();
      
      this.currentRoute = route;
      
    } catch (error) {
      console.error('Error rendering route:', error);
      if (this.notFoundCallback) {
        this.notFoundCallback(route.path);
      }
    }
  }

  /**
   * Clean up current page
   */
  cleanup() {
    // Remove all section elements added by router
    const sections = document.querySelectorAll('body > section');
    sections.forEach(section => section.remove());
    
    // Clean up global page tracking
    if (window.currentPage) {
      window.currentPage = '';
    }
  }

  /**
   * Get current path from URL
   * @returns {string} - Current path
   */
  getCurrentPath() {
    const fullPath = window.location.pathname;
    if (fullPath.startsWith(this.basePath)) {
      return fullPath.slice(this.basePath.length) || '/';
    }
    return fullPath;
  }

  /**
   * Normalize path (ensure it starts with / and ends without / unless it's root)
   * @param {string} path - Path to normalize
   * @returns {string} - Normalized path
   */
  normalizePath(path) {
    if (!path) return '/';
    if (!path.startsWith('/')) path = '/' + path;
    if (path.length > 1 && path.endsWith('/')) path = path.slice(0, -1);
    return path;
  }

  /**
   * Go back in history
   */
  back() {
    window.history.back();
  }

  /**
   * Go forward in history
   */
  forward() {
    window.history.forward();
  }
}

// Export for global use
window.Router = Router;