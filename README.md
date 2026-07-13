<h1 align="center">Certificadazo</h1>

<p align="center">
  <img src="https://github.com/juda-dev/juda-dev/blob/main/Logo-JuDa-Dev-Fondo-Transparente.png?raw=true" alt="Certificadazo logo" width="140"/>
</p>

<p align="center"><i>An AI-assisted platform to create, manage, and validate certificates, without the headache.</i></p>

<p align="center">
  <a href="#" onclick="return false;"><img src="https://img.shields.io/badge/status-in_development-FFB300?style=for-the-badge&logo=github&logoColor=black" alt="Status: In Development"/></a>
</p>

<p align="center">
  <a href="#" onclick="return false;"><img src="https://img.shields.io/badge/Java_25-E76F00?style=flat&logo=openjdk&logoColor=white&labelColor=7A3800" alt="Java 25"/></a>&nbsp;
  <a href="#" onclick="return false;"><img src="https://img.shields.io/badge/Spring_Boot_4.1.0-6DB33F?style=flat&logo=springboot&logoColor=white&labelColor=1B5E20" alt="Spring Boot 4.1.0"/></a>&nbsp;
  <a href="#" onclick="return false;"><img src="https://img.shields.io/badge/Spring_AI_2.0.0-6DB33F?style=flat&logo=spring&logoColor=white&labelColor=1B5E20" alt="Spring AI 2.0.0"/></a>&nbsp;
  <a href="#" onclick="return false;"><img src="https://img.shields.io/badge/MySQL_9-1565C0?style=flat&logo=mysql&logoColor=white&labelColor=0D2E6F" alt="MySQL 9"/></a>&nbsp;
  <a href="#" onclick="return false;"><img src="https://img.shields.io/badge/Keycloak_25-4D4D4D?style=flat&logo=keycloak&logoColor=white&labelColor=1A1A1A" alt="Keycloak 25"/></a>&nbsp;
  <a href="#" onclick="return false;"><img src="https://img.shields.io/badge/Apache_Kafka-231F20?style=flat&logo=apachekafka&logoColor=white&labelColor=000000" alt="Apache Kafka"/></a>&nbsp;
  <a href="#" onclick="return false;"><img src="https://img.shields.io/badge/Docker-2496ED?style=flat&logo=docker&logoColor=white&labelColor=0D3B6E" alt="Docker"/></a>
</p>

<br/>

## 🤔 What's this all about?

Ever had to make a certificate by hand, then chase people down to send it, then have no real way to prove it's legit? That's the exact headache **Certificadazo** is here to solve.

It's a platform where a department can design a certificate template once, plug in the data, and generate polished, official-looking certificates in bulk, then let anyone check if a certificate is real with a simple public validation link. No more *"is this PDF actually official?"* moments.

<br/>

## 👥 Who's it for?

The platform is built around four kinds of people:

| Who | What they can do |
|---|---|
| 🛠️ **Admin** | Manages users, manages departments, configures API keys |
| 🏢 **Department Manager** | Manages their department, manages their users, creates certificate templates |
| 🙋 **User** | Manages their own profile, downloads their certificates |
| 🔎 **Guest** | Validates a certificate (no account needed) |

<br/>

## 🎯 The main goal

Create certificates, send them out, and let anyone validate them publicly. That's the heart of it.

<br/>

## ✅ What's in scope

- [x] Create users
- [x] Create departments
- [x] Create certificate templates
- [x] Generate certificates as PDF
- [ ] Public certificate validation

<br/>

## 🧩 How the data fits together

Here's the general idea of how everything connects behind the scenes:

```mermaid
%%{init: {
  "theme": "base",
  "themeVariables": {
    "primaryColor": "#1A1A2E",
    "primaryTextColor": "#F5F5F5",
    "primaryBorderColor": "#00D4FF",
    "lineColor": "#00D4FF",
    "secondaryColor": "#16213E",
    "tertiaryColor": "#0F3460",
    "fontSize": "14px"
  }
}}%%
erDiagram
    USER ||--o{ CERTIFICATE : owns
    USER ||--o{ INFORMATION : provides
    TEMPLATE ||--o{ INFORMATION : uses
    TEMPLATE ||--o{ CERTIFICATE : generates
    DEPARTMENT ||--o{ POSITION : has
    DEPARTMENT ||--o{ TEMPLATE : owns
    USER }o--o{ ROLE : has
    USER }o--o{ POSITION : holds
    USER }o--o{ DEPARTMENT : belongs_to
```

In plain terms:
- A **User** can belong to one or more **Departments**, hold different **Positions**, and have different **Roles**.
- A **Department** owns its own **Templates** and **Positions**.
- A **Template** is the "blueprint" used to generate **Certificates**, and can also hold extra **Information** tied to a specific user.
- A **Certificate** always points back to the user it belongs to and the template it was generated from.

<br/>

## 🗂️ Data model in detail

A closer look at each entity and the fields it holds. Click any card to expand it.

<details>
<summary>👤&nbsp;&nbsp;<b>User</b></summary>
<br/>

| Field | Type | Notes |
|---|---|---|
| `id` | `binary(16)` | 🔑 Primary Key |
| `name` | `varchar(255)` | |
| `document` | `varchar(25)` | 🔒 Unique |
| `password` | `varchar(255)` | |

</details>

<details>
<summary>🎭&nbsp;&nbsp;<b>Role</b></summary>
<br/>

| Field | Type | Notes |
|---|---|---|
| `id` | `binary(16)` | 🔑 Primary Key |
| `name` | `varchar(25)` | |

</details>

<details>
<summary>🏢&nbsp;&nbsp;<b>Department</b></summary>
<br/>

| Field | Type | Notes |
|---|---|---|
| `id` | `binary(16)` | 🔑 Primary Key |
| `name` | `varchar(255)` | |

</details>

<details>
<summary>💼&nbsp;&nbsp;<b>Position</b></summary>
<br/>

| Field | Type | Notes |
|---|---|---|
| `id` | `binary(16)` | 🔑 Primary Key |
| `name` | `varchar(255)` | |
| `department_id` | `binary(16)` | 🔗 Foreign Key → Department |

</details>

<details>
<summary>📄&nbsp;&nbsp;<b>Template</b></summary>
<br/>

| Field | Type | Notes |
|---|---|---|
| `id` | `binary(16)` | 🔑 Primary Key |
| `name` | `varchar(255)` | 🔒 Unique |
| `design` | `varchar(255)` | |
| `department_id` | `binary(16)` | 🔗 Foreign Key → Department |
| `preview_src` | `varchar(255)` | 🔒 Unique |
| `fields` | `JSON` | |
| `images_src` | `JSON` | |

</details>

<details>
<summary>🧾&nbsp;&nbsp;<b>Information</b></summary>
<br/>

| Field | Type | Notes |
|---|---|---|
| `id` | `binary(16)` | 🔑 Primary Key |
| `data` | `JSON` | |
| `template_id` | `binary(16)` | 🔗 Foreign Key → Template |
| `user_id` | `binary(16)` | 🔗 Foreign Key → User |

</details>

<details>
<summary>🎓&nbsp;&nbsp;<b>Certificate</b></summary>
<br/>

| Field | Type | Notes |
|---|---|---|
| `id` | `binary(16)` | 🔑 Primary Key |
| `code` | `varchar(20)` | 🔒 Unique |
| `resource` | `varchar(255)` | 🔒 Unique |
| `issue_date` | `datetime` | |
| `expiration_date` | `datetime` | |
| `user_id` | `binary(16)` | 🔗 Foreign Key → User |
| `template_id` | `binary(16)` | 🔗 Foreign Key → Template |

</details>

<details>
<summary>🔗&nbsp;&nbsp;<b>Relationship tables</b></summary>
<br/>

**User ↔ Position**

| Field | Type | Notes |
|---|---|---|
| `user_id` | `binary(16)` | 🔑 Composite Primary Key |
| `position_id` | `binary(16)` | 🔑 Composite Primary Key |

**User ↔ Role**

| Field | Type | Notes |
|---|---|---|
| `user_id` | `binary(16)` | 🔑 Composite Primary Key |
| `role_id` | `binary(16)` | 🔑 Composite Primary Key |

**User ↔ Department**

| Field | Type | Notes |
|---|---|---|
| `user_id` | `binary(16)` | 🔑 Composite Primary Key |
| `department_id` | `binary(16)` | 🔑 Composite Primary Key |

</details>

<br/>

## 🛠️ Built with

- **Java 25** + **Spring Boot 4.1.0** - the backbone of the backend
- **Spring AI 2.0.0** - helping automate and smooth out certificate generation
- **MySQL 9** - where all the data lives
- **Keycloak 25** - handling authentication and access control
- **Apache Kafka** - keeping things async and event-driven between services
- **Docker** - so it runs the same everywhere

<br/>

## 🚧 Project status

Still cooking! This project is actively being built out: the core design, database structure, and user flows are already mapped out, and features are being added step by step. Screenshots and a proper walkthrough are coming soon once the UI takes shape.

<br/>

## 👨‍💻 Contact / Author

**JuDa Dev** - Full Stack Developer  
[![Email](https://img.shields.io/badge/Email-D14836?logo=gmail&logoColor=white)](mailto:judadev@proton.me)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-0A66C2?logo=linkedin)](https://www.linkedin.com/in/judadev/)  

> **Interested in collaborating or have questions?** Don't hesitate to contact me via email or LinkedIn.
