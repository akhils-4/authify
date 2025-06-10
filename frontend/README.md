# Authify Frontend

A secure authentication and authorization system built with React and Vite, providing a modern solution for user management and access control.

## Features

- User registration and authentication
- Email verification system
- Password reset functionality
- Role-based access control
- Protected routes
- Modern UI with Bootstrap
- Real-time form validation
- Toast notifications

## Tech Stack

- React 18
- Vite
- React Router v6
- Axios
- Bootstrap 5
- React Toastify
- React Hook Form
- Zod validation

## Getting Started

1. Clone the repository
2. Install dependencies:

```bash
npm install
```

3. Set up environment variables:

```bash
VITE_API_URL=http://localhost:8080/api/v1
VITE_APP_NAME=Authify
```

4. Start development server:

```bash
npm run dev
```

## Available Scripts

- `npm run dev` - Start development server
- `npm run build` - Create production build
- `npm run lint` - Run ESLint
- `npm run preview` - Preview production build
- `npm test` - Run tests

## Project Structure

```
src/
├── assets/        # Static files and images
├── components/    # Reusable UI components
├── contexts/      # React context providers
├── hooks/         # Custom React hooks
├── pages/         # Page components
├── services/      # API service layer
├── utils/         # Helper functions
└── validation/    # Form validation schemas
```

## Core Features

### Authentication Flow

- Registration with email verification
- Secure login with JWT
- Password reset functionality
- Remember me option
- Session management

### Security Features

- CSRF protection
- Rate limiting
- Input validation
- Secure password policies
- HTTP-only cookies

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

MIT License
