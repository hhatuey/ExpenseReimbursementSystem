window.appStore = {};
let currentPage = "";
const template = document.getElementById("template");

// Initialize user session from localStorage
function initializeUserSession() {
  const savedUser = localStorage.getItem('ers_user');
  if (savedUser) {
    try {
      appStore.user = JSON.parse(savedUser);
    } catch (error) {
      console.warn('Failed to parse saved user session:', error);
      localStorage.removeItem('ers_user');
    }
  }
}

// Save user session to localStorage
function saveUserSession(user) {
  if (user) {
    localStorage.setItem('ers_user', JSON.stringify(user));
  } else {
    localStorage.removeItem('ers_user');
  }
}

// Initialize router
const router = new Router();

// Define application routes
router.addRoute('/', Landing, {
  title: 'Expense Reimbursement System - Home'
});

router.addRoute('/login', Login, {
  title: 'Expense Reimbursement System - Login',
  requiresAuth: false
});

router.addRoute('/register', Registration, {
  title: 'Expense Reimbursement System - Register',
  requiresAuth: false
});

router.addRoute('/dashboard', Homeboard, {
  title: 'Expense Reimbursement System - Dashboard',
  requiresAuth: true,
  roles: ['EMPLOYEE']
});

router.addRoute('/manager', ManagerBoard, {
  title: 'Expense Reimbursement System - Manager Dashboard',
  requiresAuth: true,
  roles: ['MANAGER']
});

router.addRoute('/employees', AllEmployees, {
  title: 'Expense Reimbursement System - All Employees',
  requiresAuth: true,
  roles: ['MANAGER']
});

// Authentication guard - check if user is logged in
const authGuard = (route) => {
  if (!route.options.requiresAuth) {
    return true; // Public route
  }
  
  // Ensure user session is initialized
  initializeUserSession();
  
  const user = window.appStore.user;
  if (!user) {
    return false; // Not logged in
  }
  
  // Check role requirements
  if (route.options.roles && route.options.roles.length > 0) {
    return route.options.roles.includes(user.roleType);
  }
  
  return true; // Authenticated and role requirements met
};

// Add auth guard to router
router.addGuard(authGuard);

// 404 handler
router.setNotFound((path) => {
  console.log(`404 - Route not found: ${path}`);
  // Create a simple 404 page
  document.body.innerHTML = `
    <div class="d-flex align-items-center justify-content-center vh-100">
      <div class="text-center">
        <h1 class="display-1 fw-bold">404</h1>
        <h2 class="mb-4">Page Not Found</h2>
        <p class="lead mb-4">The page you're looking for doesn't exist.</p>
        <button onclick="router.navigate('/')" class="btn btn-primary">Go Home</button>
      </div>
    </div>
  `;
});

// Global dark mode toggle handler using event delegation
function setupDarkModeToggle() {
  const html = document.documentElement;
  
  // Check for saved theme preference or default to light
  const currentTheme = localStorage.getItem('bs-theme') || 'light';
  html.setAttribute('data-bs-theme', currentTheme);
  updateDarkModeIcon(currentTheme);
}

function toggleTheme() {
  const html = document.documentElement;
  const currentTheme = html.getAttribute('data-bs-theme');
  const newTheme = currentTheme === 'light' ? 'dark' : 'light';
  
  html.setAttribute('data-bs-theme', newTheme);
  localStorage.setItem('bs-theme', newTheme);
  updateDarkModeIcon(newTheme);
}

// Set up event delegation immediately
if (document.readyState === 'loading') {
  document.addEventListener('DOMContentLoaded', function() {
    document.addEventListener('click', function(event) {
      if (event.target.id === 'darkModeToggle' || event.target.closest('#darkModeToggle')) {
        event.preventDefault();
        toggleTheme();
      }
    });
  });
} else {
  document.addEventListener('click', function(event) {
    if (event.target.id === 'darkModeToggle' || event.target.closest('#darkModeToggle')) {
      event.preventDefault();
      toggleTheme();
    }
  });
}

// Dark mode functionality
function initDarkMode() {
  setupDarkModeToggle();
}

function updateDarkModeIcon(theme) {
  const darkModeToggles = document.querySelectorAll('[id="darkModeToggle"]');
  const icon = theme === 'light' ? '🌙' : '☀️';
  
  darkModeToggles.forEach(toggle => {
    toggle.textContent = icon;
  });
}

// #region HTML sections handlers
function PageLayout(layoutName) {
  const layout = template.content.getElementById(layoutName).cloneNode(true);
  this.getLayout = () => layout;

  this.render = () => {
    currentPage = layoutName;
    document.body.prepend(layout);
    
    // Initialize dark mode for new page
    if (typeof initDarkMode === 'function') {
      initDarkMode();
    }
  };
}

function Landing() {
  PageLayout.call(this, "landing");
  const landing = this.getLayout();
  validate(landing);

  const btnLogin = landing.querySelector("#btnlogin");
  btnLogin.addEventListener("click", (event) => {
    event.preventDefault();
    router.navigate('/login');
  });

  const btnSignUp = landing.querySelector("#btnsignup");
  btnSignUp.addEventListener("click", (event) => {
    event.preventDefault();
    router.navigate('/register');
  });
}

function Login() {
  PageLayout.call(this, "login");
  const login = this.getLayout();
  validate(login);

  const btnSignIn = login.querySelector("#btnsignin");
  btnSignIn.addEventListener("click", (event) => {
    event.preventDefault();
    let dataForm = new FormData(login.querySelector("form"));
    let obj = Object.fromEntries(dataForm.entries());

    sendPost("/ers/api/login", obj).then((data) => {
      if (!data) {
        login.querySelector(".alert").classList.replace("d-none", "d-block");
        return;
      }
      appStore.user = data;
      saveUserSession(data); // Save to localStorage
      
      // Navigate based on user role
      if (data.roleType === "MANAGER") {
        router.navigate('/manager');
      } else {
        router.navigate('/dashboard');
      }
    });
  });
}

function Homeboard() {
  PageLayout.call(this, "homeboard");
  const homeboard = this.getLayout();
  document.title = "Home Page";
  let user = appStore.user;
  initDarkMode();
  populate();

  homeboard.querySelector(".navbar-brand").innerHTML =
    user.firstName + "`s Dashboard";

  let myModal = homeboard.querySelector("#myModal");
  let select = myModal.querySelector("select");
  let txtAmount = myModal.querySelector("#amount");
  let textArea = myModal.querySelector("textarea");

  myModal.querySelector("#btnsave").addEventListener("click", function (event) {
    event.preventDefault();
    myModal.querySelector("#nan").classList.replace("d-block", "d-none");
    myModal.querySelector("#error").classList.replace("d-block", "d-none");
    let amount = Number.parseFloat(txtAmount.value);
    if (!amount) {
      myModal.querySelector("#nan").classList.replace("d-none", "d-block");
      return;
    }
    let obj = {
      id: 0,
      amount: amount,
      submitted: Date.now(),
      resolved: null,
      description: textArea.value,
      receipt: 0,
      sign: "",
      resolverID: null,
      statusID: 1,
      authorID: user.id,
      typeID: select.selectedIndex,
    };
    sendPost("/ers/api/save", obj)
      .then((data) => {
        console.log(data);
        populate();
        myModal.querySelector("form").reset();
        myModal.querySelector("#btnclose").click();
      })
      .catch((e) => {
        console.log(e);
        myModal.querySelector("#error").classList.replace("d-none", "d-block");
      });
  });

  fetch("/ers/api/types")
    .then((response) => response.json())
    .then((data) => {
      data.map((itm) => {
        let clone = select.querySelector("option").cloneNode(true);
        clone.selected = false;
        clone.text = itm.name;
        select.options.add(clone, itm.id);
      });
    });

  homeboard
    .querySelector("#logout")
    .addEventListener("click", function (event) {
      event.preventDefault();
      fetch("/ers/api/logout")
        .then((response) => response.json())
        .then((data) => {
          console.log(data);
          appStore.user = {};
          saveUserSession(null); // Clear from localStorage
          router.navigate('/login');
        });
    });

  homeboard
    .querySelector("#navbarSideCollapse")
    .addEventListener("click", function () {
      homeboard.querySelector(".offcanvas-collapse").classList.toggle("open");
    });
  let divPending = homeboard.querySelector("#pending");
  let divExpenses = divPending.querySelector("#expenses");
  let divItem = divExpenses.querySelector(".item");

  let divResolved = homeboard.querySelector("#resolved");
  let divExpensesR = divResolved.querySelector("#expenses-r");
  let divItemR = divExpensesR.querySelector(".item-r");
  function populate() {
    fetch("/ers/api/home?userid=" + user.id)
      .then((response) => response.json())
      .then((data) => {
        if (!data) {
          divExpenses.innerHTML = "<p>Nothing to show</p>";
          return;
        }
        console.log(data);
        divExpenses.innerHTML = "";
        data
          .filter((item) => item.resolved === null)
          .map((itm) => {
            let clone = divItem.cloneNode(true);
            clone.addEventListener("click", itemHandler);
            clone.querySelector("strong").innerHTML = itm.type;
            clone.querySelector("p").innerHTML += itm.description;
            clone.querySelector("#date").innerHTML = new Date(
              itm.submitted,
            ).toDateString();
            divExpenses.append(clone);
          });
        divExpensesR.innerHTML = "";
        data
          .filter((item) => item.resolved !== null)
          .map((itm) => {
            let clone = divItemR.cloneNode(true);
            let color = itm.status === "DENIED" ? "red" : "green";
            clone.querySelector("rect").setAttribute("fill", color);
            // clone.addEventListener('click', itemHandler)
            clone.querySelector("strong").innerHTML = itm.type;
            clone.querySelector("#user").innerHTML = itm.author;
            clone.querySelector("p").innerHTML = itm.description;
            clone.querySelector("#r-date").innerHTML = new Date(
              itm.resolved,
            ).toDateString();
            clone.querySelector("#resolver").innerHTML = itm.resolver;
            divExpensesR.append(clone);
          });
      });
  }

  function itemHandler(event) {
    event.preventDefault();
  }
}

function ManagerBoard() {
  PageLayout.call(this, "managerboard");
  const managerboard = this.getLayout();
  document.title = "Manager Page";
  let user = appStore.user;
  initDarkMode();
  populate();

  managerboard.querySelector(".navbar-brand").innerHTML =
    user.firstName + "`s Dashboard";

  managerboard
    .querySelector("#logout")
    .addEventListener("click", function (event) {
      event.preventDefault();
      fetch("/ers/api/logout")
        .then((response) => response.json())
        .then((data) => {
          console.log(data);
          appStore.user = {};
          saveUserSession(null); // Clear from localStorage
          router.navigate('/login');
        });
    });

  managerboard
    .querySelector("#navbarSideCollapse")
    .addEventListener("click", function () {
      managerboard
        .querySelector(".offcanvas-collapse")
        .classList.toggle("open");
    });

  let divPendings = managerboard.querySelector("#pendings");
  let divExpensesP = divPendings.querySelector("#expenses-p");
  let divItemP = divExpensesP.querySelector(".item-p");

  let divResolved = managerboard.querySelector("#resolved");
  let divExpensesR = divResolved.querySelector("#expenses-r");
  let divItemR = divExpensesR.querySelector(".item-r");

  function populate() {
    fetch("/ers/api/manager")
      .then((response) => response.json())
      .then((data) => {
        if (!data) {
          divExpenses.innerHTML = "<p>Nothing to show</p>";
          return;
        }
        console.log(data);
        divExpensesP.innerHTML = "";
        data
          .filter((item) => item.resolved === null)
          .map((itm) => {
            let clone = divItemP.cloneNode(true);
            clone.setAttribute("id", itm.id);
            clone.addEventListener("click", itemHandler);
            clone.querySelector("strong").innerHTML = itm.type;
            clone.querySelector("p").innerHTML += itm.description;
            clone.querySelector("#s-date").innerHTML = new Date(
              itm.submitted,
            ).toDateString();
            clone.querySelector("#author").innerHTML = itm.author;
            divExpensesP.append(clone);
          });
        divExpensesR.innerHTML = "";
        data
          .filter((item) => item.resolved !== null)
          .map((itm) => {
            let clone = divItemR.cloneNode(true);
            let color = itm.status === "DENIED" ? "red" : "green";
            clone.querySelector("rect").setAttribute("fill", color);
            // clone.addEventListener('click', itemHandler)
            clone.querySelector("strong").innerHTML = itm.type;
            clone.querySelector("#user").innerHTML = itm.author;
            clone.querySelector("p").innerHTML = itm.description;
            clone.querySelector("#r-date").innerHTML = new Date(
              itm.resolved,
            ).toDateString();
            clone.querySelector("#resolver").innerHTML = itm.resolver;
            divExpensesR.append(clone);
          });
      });
  }

  let divEmployees = managerboard.querySelector("#employees");
  let divList = divEmployees.querySelector("#list");
  let divItemE = divList.querySelector(".item-e");

  function employees() {
    fetch("/ers/api/employees")
      .then((response) => response.json())
      .then((data) => {
        console.log(data);
        divList.innerHTML = "";
        data.map((itm) => {
          let name = itm.firstName + " " + itm.lastName;
          let clone = divItemE.cloneNode(true);
          clone.setAttribute("id", itm.id);
          clone.setAttribute("data-name", name);
          clone.addEventListener("click", oneHandler);
          clone.querySelector("strong").innerHTML = name;
          clone.querySelector("p").innerHTML += itm.roleType;
          divList.append(clone);
        });
      });
  }
  employees();
  let empExpenses = managerboard.querySelector("#emp-expenses");
  let list = empExpenses.querySelector("#list-expenses");
  empExpenses
    .querySelector("#btnclose")
    .addEventListener("click", function (event) {
      event.preventDefault();
      empExpenses.classList.replace("d-block", "d-none");
    });
  function oneHandler(event) {
    event.preventDefault();
    let name = event.currentTarget.getAttribute("data-name");
    let item = list.querySelector(".item-expenses");
    fetch("/ers/api/ticketbyname?field=author&name=" + name)
      .then((response) => response.json())
      .then((data) => {
        console.log(data);
        // list.innerHTML = ''
        if (data) {
          data.map((itm) => {
            let clone = item.cloneNode(true);
            clone.querySelector("strong").innerHTML = itm.type;
            clone.querySelector("p").innerHTML += itm.description;
            clone.querySelector("#s-date").innerHTML = new Date(
              itm.submitted,
            ).toDateString();
            clone.querySelector("#author").innerHTML = itm.resolver;
            list.append(clone);
          });
        }

        empExpenses.classList.replace("d-none", "d-block");
      });
  }

  let itmModal = managerboard.querySelector("#itmModal");
  itmModal
    .querySelector("#btnsave")
    .addEventListener("click", function (event) {
      let radioValue = itmModal.querySelector(
        'input[name="inlineRadioOptions"]:checked',
      ).value;
      let obj = {
        id: itmModal.getAttribute("data-item-id"),
        resolved: Date.now(),
        resolver: appStore.user.id,
        status: radioValue,
      };
      sendPost("/ers/api/update", obj).then((data) => {
        console.log(data);
        itmModal.querySelector("#btnclose").click();
        populate();
      });
    });

  async function itemHandler(event) {
    event.preventDefault();
    event.stopPropagation();
    console.log(event.currentTarget.id);
    let res = await fetch(
      "/ers/api/ticket?field=reimb_id&id=" + event.currentTarget.id,
    );
    let data = await res.json();
    itmModal.querySelector("#s-date").innerHTML = new Date(
      data[0].submitted,
    ).toDateString();
    itmModal.querySelector(".card-title").innerHTML = data[0].type;
    itmModal.querySelector(".card-subtitle").innerHTML = "by " + data[0].author;
    itmModal.querySelector(".card-text").innerHTML = data[0].description;
    itmModal.querySelector("#s-amount").innerHTML = data[0].amount;
    itmModal.setAttribute("data-item-id", data[0].id);
    new bootstrap.Modal(itmModal).show();
  }
}

function Registration() {
  PageLayout.call(this, "registration");
  const registration = this.getLayout();
  validate(registration);

  const btnRegister = registration.querySelector("#btnregister");
  btnRegister.addEventListener("click", function (event) {
    event.preventDefault();
    validate(registration);
    let form = registration.querySelector("form");
    if (!form.checkValidity()) return;
    const data = new FormData(form);
    const obj = Object.fromEntries(data.entries());
    console.log(obj);
    sendPost("/ers/api/register", obj).then((data) => {
      if (!data) return;
      console.log(data);
      router.navigate('/login');
    });
  });
}

function AllEmployees() {
  PageLayout.call(this, "allemployees");
  const allemployees = this.getLayout();
  
  // Basic navigation back to manager dashboard
  const backButton = allemployees.querySelector('a[href="#"]');
  if (backButton) {
    backButton.addEventListener('click', (e) => {
      e.preventDefault();
      router.navigate('/manager');
    });
  }
}

// Router will handle page loading and history management automatically

// Example starter JavaScript for disabling form submissions if there are invalid fields
function validate(page) {
  // Fetch all the forms we want to apply custom Bootstrap validation styles to
  var forms = page.querySelectorAll(".needs-validation");
  // Loop over them and prevent submission
  Array.prototype.slice.call(forms).forEach(function (form) {
    form.addEventListener(
      "click",
      function (event) {
        if (!form.checkValidity()) {
          event.preventDefault();
          event.stopPropagation();
        }
        form.classList.add("was-validated");
      },
      false,
    );
  });
}

async function sendPost(url, data) {
  let res = await fetch(url, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  });
  let jsonData = await res.json();
  return jsonData;
}
