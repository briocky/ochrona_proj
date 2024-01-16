import Link from 'next/link';

export default function Home() {
  return (
    <div>
      <header className="bg-blue-500 text-white text-center py-20">
        <h1 className="text-4xl font-bold mb-4">
          Your Trusted Partner in Loans
        </h1>
        <p className="text-lg">
          Get the financial support from people you trust.
        </p>
        <Link
          href="/auth/register"
          className="mt-6 bg-white text-blue-500 py-2 px-4 rounded-full inline-block"
        >
          Join Now
        </Link>
      </header>

      <section className="container mx-auto py-16 text-center">
        <h2 className="text-3xl font-bold mb-8">Why Choose Loan Manager?</h2>
        <p className="text-gray-600 mb-8">
          You decide who you borrow from and when you pay back the money.
        </p>
        <Link
          href="/auth/register"
          className="bg-blue-500 text-white py-2 px-4 rounded-full inline-block"
        >
          Join Now
        </Link>
      </section>

      <footer className="bg-gray-800 text-white text-center py-4">
        <p>&copy; 2024 Loan Manager. All rights reserved.</p>
      </footer>
    </div>
  );
}
